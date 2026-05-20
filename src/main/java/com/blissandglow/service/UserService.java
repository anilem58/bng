package com.blissandglow.service;

import com.blissandglow.dao.UserDAO;
import com.blissandglow.model.User;
import com.blissandglow.util.PasswordUtil;
import com.blissandglow.util.ValidationUtil;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    /** Register a new customer. Returns null on success, or an error message. */
    public String register(User user, String plainPassword) throws SQLException {
        if (ValidationUtil.isBlank(user.getFullName()))         return "Full name is required.";
        if (!ValidationUtil.isValidEmail(user.getEmail()))       return "Invalid email address.";
        if (!ValidationUtil.isValidPhone(user.getPhone()))       return "Phone must be 7–15 digits.";
        if (!ValidationUtil.isValidPassword(plainPassword))
            return "Password must be at least 8 characters with upper, lower, digit, and special character.";

        if (userDAO.emailExists(user.getEmail(), 0))  return "Email is already registered.";
        if (userDAO.phoneExists(user.getPhone(), 0))  return "Phone number is already registered.";

        user.setPasswordHash(PasswordUtil.hash(plainPassword));
        user.setRole("CUSTOMER");
        user.setStatus("PENDING");
        userDAO.insert(user);
        return null;
    }

    /** Authenticate a user. Returns the User or null; throws for DB errors. */
    public User login(String email, String plainPassword) throws SQLException {
        if (ValidationUtil.isBlank(email) || ValidationUtil.isBlank(plainPassword)) return null;
        User user = userDAO.findByEmail(email.trim().toLowerCase());
        if (user == null) return null;
        if (!PasswordUtil.verify(plainPassword, user.getPasswordHash())) return null;
        return user;
    }

    public User findById(int userId) throws SQLException {
        return userDAO.findById(userId);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.findAll();
    }

    public List<User> getPendingUsers() throws SQLException {
        return userDAO.findPending();
    }

    /** Update profile fields. Returns null on success, or an error message. */
    public String updateProfile(User user) throws SQLException {
        if (ValidationUtil.isBlank(user.getFullName())) return "Full name is required.";
        if (!ValidationUtil.isValidPhone(user.getPhone())) return "Phone must be 7–15 digits.";
        if (userDAO.phoneExists(user.getPhone(), user.getUserId())) return "Phone already in use by another account.";
        userDAO.updateProfile(user);
        return null;
    }

    /** Change password. Returns null on success, or an error message. */
    public String changePassword(int userId, String currentPlain, String newPlain) throws SQLException {
        User user = userDAO.findById(userId);
        if (user == null) return "User not found.";
        if (!PasswordUtil.verify(currentPlain, user.getPasswordHash())) return "Current password is incorrect.";
        if (!ValidationUtil.isValidPassword(newPlain))
            return "New password must be at least 8 characters with upper, lower, digit, and special character.";
        userDAO.updatePassword(userId, PasswordUtil.hash(newPlain));
        return null;
    }

    public void approveUser(int userId) throws SQLException {
        userDAO.updateStatus(userId, "APPROVED");
    }

    public void rejectUser(int userId) throws SQLException {
        userDAO.updateStatus(userId, "REJECTED");
    }

    public int countCustomers() throws SQLException { return userDAO.countCustomers(); }
    public int countPending()   throws SQLException { return userDAO.countPending(); }
}
