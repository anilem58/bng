package com.blissandglow.dao;

import com.blissandglow.model.ContactMessage;
import com.blissandglow.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {

    private ContactMessage map(ResultSet rs) throws SQLException {
        ContactMessage m = new ContactMessage();
        m.setMessageId(rs.getInt("message_id"));
        m.setName(rs.getString("name"));
        m.setEmail(rs.getString("email"));
        m.setSubject(rs.getString("subject"));
        m.setMessage(rs.getString("message"));
        Timestamp ts = rs.getTimestamp("submitted_at");
        if (ts != null) m.setSubmittedAt(ts.toLocalDateTime());
        m.setRead(rs.getBoolean("is_read"));
        return m;
    }

    public void insert(ContactMessage msg) throws SQLException {
        String sql = "INSERT INTO contact_messages (name, email, subject, message) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, msg.getName());
            ps.setString(2, msg.getEmail());
            ps.setString(3, msg.getSubject());
            ps.setString(4, msg.getMessage());
            ps.executeUpdate();
        }
    }

    public List<ContactMessage> findAll() throws SQLException {
        List<ContactMessage> list = new ArrayList<>();
        String sql = "SELECT * FROM contact_messages ORDER BY submitted_at DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public void markAsRead(int messageId) throws SQLException {
        String sql = "UPDATE contact_messages SET is_read=TRUE WHERE message_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, messageId);
            ps.executeUpdate();
        }
    }

    public int countUnread() throws SQLException {
        String sql = "SELECT COUNT(*) FROM contact_messages WHERE is_read=FALSE";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}
