package com.blissandglow.util;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class ResetAdminPassword {
    public static void main(String[] args) throws Exception {
        String newHash = BCrypt.hashpw("Admin@123", BCrypt.gensalt(10));

        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/blissandglow_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                "root", "");
             PreparedStatement ps = c.prepareStatement(
                "UPDATE users SET password_hash=? WHERE email=?")) {
            ps.setString(1, newHash);
            ps.setString(2, "admin@blissandglow.com");
            int rows = ps.executeUpdate();
            System.out.println("Done. Rows updated: " + rows);
            System.out.println("Login with: Admin@123");
        }
    }
}
