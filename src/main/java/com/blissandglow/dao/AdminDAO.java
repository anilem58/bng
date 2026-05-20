package com.blissandglow.dao;

import com.blissandglow.model.Product;
import com.blissandglow.model.User;
import com.blissandglow.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AdminDAO - Data Access Object for all admin-related database operations.
 * Handles CRUD operations for users and products in the admin panel.
 *
 * Architecture: AdminServlet → AdminDAO → MySQL (via DBConnection)
 *
 * Author: Member 5 (Admin + Security)
 */
public class AdminDAO {

    // ═══════════════════════════════════════════════════════
    //  USER MANAGEMENT
    // ═══════════════════════════════════════════════════════

    /**
     * Retrieves all registered users from the database.
     *
     * @return list of all User objects
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, email, full_name, phone, role, status, created_at FROM users ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }

        } catch (SQLException e) {
            System.err.println("[AdminDAO] Error fetching all users: " + e.getMessage());
        }
        return users;
    }

    /**
     * Retrieves a single user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the User object, or null if not found
     */
    public User getUserById(int userId) {
        String sql = "SELECT user_id, username, email, full_name, phone, role, status, created_at FROM users WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("[AdminDAO] Error fetching user by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Updates the role and status of an existing user.
     *
     * @param userId the ID of the user to update
     * @param role   the new role ("admin" or "customer")
     * @param status the new status ("active" or "inactive")
     * @return true if update was successful
     */
    public boolean updateUser(int userId, String role, String status) {
        String sql = "UPDATE users SET role = ?, status = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, role);
            ps.setString(2, status);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[AdminDAO] Error updating user: " + e.getMessage());
        }
        return false;
    }

    /**
     * Deletes a user from the database by their ID.
     *
     * @param userId the ID of the user to delete
     * @return true if deletion was successful
     */
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[AdminDAO] Error deleting user: " + e.getMessage());
        }
        return false;
    }

    /**
     * Returns the total number of registered users.
     */
    public int getTotalUsers() {
        String sql = "SELECT COUNT(*) FROM users";
        return executeCountQuery(sql);
    }

    // ═══════════════════════════════════════════════════════
    //  PRODUCT MANAGEMENT
    // ═══════════════════════════════════════════════════════

    /**
     * Retrieves all products from the database.
     *
     * @return list of all Product objects
     */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT product_id, name, description, price, stock, category, image_url, created_at FROM products ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(mapRowToProduct(rs));
            }

        } catch (SQLException e) {
            System.err.println("[AdminDAO] Error fetching all products: " + e.getMessage());
        }
        return products;
    }

    /**
     * Retrieves a single product by its ID.
     *
     * @param productId the ID of the product to retrieve
     * @return the Product object, or null if not found
     */
    public Product getProductById(int productId) {
        String sql = "SELECT product_id, name, description, price, stock, category, image_url, created_at FROM products WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToProduct(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("[AdminDAO] Error fetching product by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Adds a new product to the database.
     *
     * @param product the Product object to insert
     * @return true if insertion was successful
     */
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO products (name, description, price, stock, category, image_url) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getStock());
            ps.setString(5, product.getCategory());
            ps.setString(6, product.getImageUrl());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[AdminDAO] Error adding product: " + e.getMessage());
        }
        return false;
    }

    /**
     * Updates an existing product in the database.
     *
     * @param product the Product object with updated fields
     * @return true if update was successful
     */
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock = ?, category = ?, image_url = ? WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getStock());
            ps.setString(5, product.getCategory());
            ps.setString(6, product.getImageUrl());
            ps.setInt(7, product.getProductId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[AdminDAO] Error updating product: " + e.getMessage());
        }
        return false;
    }

    /**
     * Deletes a product from the database by its ID.
     *
     * @param productId the ID of the product to delete
     * @return true if deletion was successful
     */
    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[AdminDAO] Error deleting product: " + e.getMessage());
        }
        return false;
    }

    /**
     * Returns the total number of products in the store.
     */
    public int getTotalProducts() {
        String sql = "SELECT COUNT(*) FROM products";
        return executeCountQuery(sql);
    }

    /**
     * Returns the total number of orders placed.
     */
    public int getTotalOrders() {
        String sql = "SELECT COUNT(*) FROM orders";
        return executeCountQuery(sql);
    }

    /**
     * Returns the total revenue from all completed orders.
     */
    public double getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM orders WHERE status = 'completed'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            System.err.println("[AdminDAO] Error fetching revenue: " + e.getMessage());
        }
        return 0.0;
    }

    // ═══════════════════════════════════════════════════════
    //  HELPERS
    // ═══════════════════════════════════════════════════════

    /**
     * Maps a ResultSet row to a User object.
     */
    private User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setFullName(rs.getString("full_name"));
        user.setPhone(rs.getString("phone"));
        user.setRole(rs.getString("role"));
        user.setStatus(rs.getString("status"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    }

    /**
     * Maps a ResultSet row to a Product object.
     */
    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getDouble("price"));
        product.setStock(rs.getInt("stock"));
        product.setCategory(rs.getString("category"));
        product.setImageUrl(rs.getString("image_url"));
        product.setCreatedAt(rs.getTimestamp("created_at"));
        return product;
    }

    /**
     * Executes a COUNT(*) query and returns the result.
     */
    private int executeCountQuery(String sql) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            System.err.println("[AdminDAO] Error in count query: " + e.getMessage());
        }
        return 0;
    }
}
