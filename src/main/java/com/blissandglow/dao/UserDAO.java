package com.blissandglow.dao;

import com.blissandglow.model.User;
import com.blissandglow.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /** Map a ResultSet row to a User object. */
    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setPhone(rs.getString("phone"));
        u.setPasswordHash(rs.getString("password_hash"));
        Date dob = rs.getDate("dob");
        if (dob != null) u.setDob(dob.toLocalDate());
        u.setAddress(rs.getString("address"));
        u.setRole(rs.getString("role"));
        u.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) u.setCreatedAt(ts.toLocalDateTime());
        return u;
    }

    /** Insert a new user and return the generated primary key. */
    public int insert(User user) throws SQLException {
        String sql = "INSERT INTO users (full_name, email, phone, password_hash, dob, address, role, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPasswordHash());
            ps.setDate(5, user.getDob() != null ? Date.valueOf(user.getDob()) : null);
            ps.setString(6, user.getAddress());
            ps.setString(7, user.getRole() != null ? user.getRole() : "CUSTOMER");
            ps.setString(8, user.getStatus() != null ? user.getStatus() : "PENDING");
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    /** Find a user by their email address. */
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /** Find a user by primary key. */
    public User findById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /** Return all users (admin use). */
    public List<User> findAll() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    /** Return all users with PENDING status. */
    public List<User> findPending() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE status = 'PENDING' ORDER BY created_at ASC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    /** Update user profile fields (no password, no role, no status). */
    public void updateProfile(User user) throws SQLException {
        String sql = "UPDATE users SET full_name=?, phone=?, dob=?, address=? WHERE user_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getPhone());
            ps.setDate(3, user.getDob() != null ? Date.valueOf(user.getDob()) : null);
            ps.setString(4, user.getAddress());
            ps.setInt(5, user.getUserId());
            ps.executeUpdate();
        }
    }

    /** Update password hash only. */
    public void updatePassword(int userId, String newHash) throws SQLException {
        String sql = "UPDATE users SET password_hash=? WHERE user_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, newHash);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    /** Update a user's status (PENDING / APPROVED / REJECTED). */
    public void updateStatus(int userId, String status) throws SQLException {
        String sql = "UPDATE users SET status=? WHERE user_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    /** Check whether an email is already taken (optionally excluding one user_id). */
    public boolean emailExists(String email, int excludeUserId) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email=? AND user_id <> ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setInt(2, excludeUserId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    /** Check whether a phone number is already taken (optionally excluding one user_id). */
    public boolean phoneExists(String phone, int excludeUserId) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE phone=? AND user_id <> ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, phone);
            ps.setInt(2, excludeUserId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    /** Count total users (customers only). */
    public int countCustomers() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE role='CUSTOMER'";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /** Count PENDING approvals. */
    public int countPending() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE status='PENDING' AND role='CUSTOMER'";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}
