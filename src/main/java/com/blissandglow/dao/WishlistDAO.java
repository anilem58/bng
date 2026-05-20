package com.blissandglow.dao;

import com.blissandglow.model.Product;
import com.blissandglow.model.WishlistItem;
import com.blissandglow.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WishlistDAO {

    public List<WishlistItem> findByUserId(int userId) throws SQLException {
        List<WishlistItem> list = new ArrayList<>();
        String sql = "SELECT w.*, p.product_id, p.name, p.brand, p.price, p.stock_quantity, " +
                     "p.image_path, p.sku, p.category_id, p.description, p.created_at " +
                     "FROM wishlist w JOIN products p ON w.product_id=p.product_id " +
                     "WHERE w.user_id=? ORDER BY w.added_at DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    WishlistItem item = new WishlistItem();
                    item.setWishlistId(rs.getInt("wishlist_id"));
                    item.setUserId(rs.getInt("user_id"));
                    item.setProductId(rs.getInt("product_id"));
                    Timestamp ts = rs.getTimestamp("added_at");
                    if (ts != null) item.setAddedAt(ts.toLocalDateTime());

                    Product p = new Product();
                    p.setProductId(rs.getInt("product_id"));
                    p.setName(rs.getString("name"));
                    p.setBrand(rs.getString("brand"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setStockQuantity(rs.getInt("stock_quantity"));
                    p.setImagePath(rs.getString("image_path"));
                    p.setSku(rs.getString("sku"));
                    p.setCategoryId(rs.getInt("category_id"));
                    p.setDescription(rs.getString("description"));
                    item.setProduct(p);
                    list.add(item);
                }
            }
        }
        return list;
    }

    public void add(int userId, int productId) throws SQLException {
        String sql = "INSERT IGNORE INTO wishlist (user_id, product_id) VALUES (?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    public void remove(int userId, int productId) throws SQLException {
        String sql = "DELETE FROM wishlist WHERE user_id=? AND product_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    public boolean exists(int userId, int productId) throws SQLException {
        String sql = "SELECT 1 FROM wishlist WHERE user_id=? AND product_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }
}
