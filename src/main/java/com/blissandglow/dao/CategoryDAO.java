package com.blissandglow.dao;

import com.blissandglow.model.Category;
import com.blissandglow.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    private Category map(ResultSet rs) throws SQLException {
        Category cat = new Category();
        cat.setCategoryId(rs.getInt("category_id"));
        cat.setName(rs.getString("name"));
        cat.setDescription(rs.getString("description"));
        return cat;
    }

    public List<Category> findAll() throws SQLException {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT c.*, (SELECT COUNT(*) FROM products p WHERE p.category_id=c.category_id) AS product_count " +
                     "FROM categories c ORDER BY c.name";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Category cat = map(rs);
                cat.setProductCount(rs.getInt("product_count"));
                list.add(cat);
            }
        }
        return list;
    }

    public Category findById(int categoryId) throws SQLException {
        String sql = "SELECT * FROM categories WHERE category_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public int insert(Category category) throws SQLException {
        String sql = "INSERT INTO categories (name, description) VALUES (?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    public void update(Category category) throws SQLException {
        String sql = "UPDATE categories SET name=?, description=? WHERE category_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, category.getCategoryId());
            ps.executeUpdate();
        }
    }

    public void delete(int categoryId) throws SQLException {
        String sql = "DELETE FROM categories WHERE category_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.executeUpdate();
        }
    }

    public boolean nameExists(String name, int excludeId) throws SQLException {
        String sql = "SELECT 1 FROM categories WHERE name=? AND category_id <> ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, excludeId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }
}
