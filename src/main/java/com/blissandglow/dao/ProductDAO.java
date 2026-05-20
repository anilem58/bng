package com.blissandglow.dao;

import com.blissandglow.model.Product;
import com.blissandglow.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private Product map(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setProductId(rs.getInt("product_id"));
        p.setName(rs.getString("name"));
        p.setBrand(rs.getString("brand"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStockQuantity(rs.getInt("stock_quantity"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setImagePath(rs.getString("image_path"));
        p.setSku(rs.getString("sku"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) p.setCreatedAt(ts.toLocalDateTime());
        try { p.setCategoryName(rs.getString("category_name")); } catch (SQLException ignore) {}
        return p;
    }

    public List<Product> findByCategories(int[] categoryIds) throws SQLException {
        if (categoryIds == null || categoryIds.length == 0) return new ArrayList<>();
        StringBuilder sb = new StringBuilder(
            "SELECT p.*, c.name AS category_name FROM products p " +
            "JOIN categories c ON p.category_id=c.category_id WHERE p.category_id IN (");
        for (int i = 0; i < categoryIds.length; i++) {
            if (i > 0) sb.append(",");
            sb.append("?");
        }
        sb.append(") ORDER BY p.name");
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < categoryIds.length; i++) ps.setInt(i + 1, categoryIds[i]);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public List<Product> findAll() throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "JOIN categories c ON p.category_id=c.category_id ORDER BY p.name";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Product> findPaginated(int offset, int limit, int categoryId, String sort) throws SQLException {
        List<Product> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder(
            "SELECT p.*, c.name AS category_name FROM products p " +
            "JOIN categories c ON p.category_id=c.category_id WHERE 1=1");
        if (categoryId > 0) sb.append(" AND p.category_id=?");
        switch (sort == null ? "" : sort) {
            case "price_asc"  -> sb.append(" ORDER BY p.price ASC");
            case "price_desc" -> sb.append(" ORDER BY p.price DESC");
            case "name_asc"   -> sb.append(" ORDER BY p.name ASC");
            default           -> sb.append(" ORDER BY p.created_at DESC");
        }
        sb.append(" LIMIT ? OFFSET ?");
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sb.toString())) {
            int idx = 1;
            if (categoryId > 0) ps.setInt(idx++, categoryId);
            ps.setInt(idx++, limit);
            ps.setInt(idx, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public int countProducts(int categoryId) throws SQLException {
        String sql = categoryId > 0
            ? "SELECT COUNT(*) FROM products WHERE category_id=?"
            : "SELECT COUNT(*) FROM products";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (categoryId > 0) ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public List<Product> search(String keyword) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "JOIN categories c ON p.category_id=c.category_id " +
                     "WHERE p.name LIKE ? OR p.brand LIKE ? ORDER BY p.name";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public Product findById(int productId) throws SQLException {
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "JOIN categories c ON p.category_id=c.category_id WHERE p.product_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public int insert(Product product) throws SQLException {
        String sql = "INSERT INTO products (name, brand, description, price, stock_quantity, category_id, image_path, sku) " +
                     "VALUES (?,?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getBrand());
            ps.setString(3, product.getDescription());
            ps.setBigDecimal(4, product.getPrice());
            ps.setInt(5, product.getStockQuantity());
            ps.setInt(6, product.getCategoryId());
            ps.setString(7, product.getImagePath());
            ps.setString(8, product.getSku());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    public void update(Product product) throws SQLException {
        String sql = "UPDATE products SET name=?, brand=?, description=?, price=?, " +
                     "stock_quantity=?, category_id=?, image_path=?, sku=? WHERE product_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getBrand());
            ps.setString(3, product.getDescription());
            ps.setBigDecimal(4, product.getPrice());
            ps.setInt(5, product.getStockQuantity());
            ps.setInt(6, product.getCategoryId());
            ps.setString(7, product.getImagePath());
            ps.setString(8, product.getSku());
            ps.setInt(9, product.getProductId());
            ps.executeUpdate();
        }
    }

    public void delete(int productId) throws SQLException {
        String sql = "DELETE FROM products WHERE product_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.executeUpdate();
        }
    }

    public boolean skuExists(String sku, int excludeId) throws SQLException {
        String sql = "SELECT 1 FROM products WHERE sku=? AND product_id <> ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, sku);
            ps.setInt(2, excludeId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM products";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /** Top N products by total quantity ordered — for reports. */
    public List<Object[]> getTopOrderedProducts(int limit) throws SQLException {
        List<Object[]> result = new ArrayList<>();
        String sql = "SELECT p.name, SUM(oi.quantity) AS total_qty, SUM(oi.quantity * oi.unit_price) AS revenue " +
                     "FROM order_items oi JOIN products p ON oi.product_id=p.product_id " +
                     "GROUP BY p.product_id, p.name ORDER BY total_qty DESC LIMIT ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new Object[]{rs.getString("name"), rs.getInt("total_qty"), rs.getBigDecimal("revenue")});
                }
            }
        }
        return result;
    }

    /** Sales grouped by category — for reports. */
    public List<Object[]> getSalesByCategory() throws SQLException {
        List<Object[]> result = new ArrayList<>();
        String sql = "SELECT cat.name, SUM(oi.quantity * oi.unit_price) AS revenue, SUM(oi.quantity) AS units " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id=p.product_id " +
                     "JOIN categories cat ON p.category_id=cat.category_id " +
                     "GROUP BY cat.category_id, cat.name ORDER BY revenue DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new Object[]{rs.getString("name"), rs.getBigDecimal("revenue"), rs.getInt("units")});
            }
        }
        return result;
    }

    /** Stock vs ordered comparison — for reports. */
    public List<Object[]> getStockReport() throws SQLException {
        List<Object[]> result = new ArrayList<>();
        String sql = "SELECT p.name, p.sku, p.stock_quantity, " +
                     "COALESCE(SUM(oi.quantity),0) AS total_ordered " +
                     "FROM products p LEFT JOIN order_items oi ON p.product_id=oi.product_id " +
                     "GROUP BY p.product_id, p.name, p.sku, p.stock_quantity ORDER BY p.name";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new Object[]{rs.getString("name"), rs.getString("sku"),
                        rs.getInt("stock_quantity"), rs.getInt("total_ordered")});
            }
        }
        return result;
    }
}
