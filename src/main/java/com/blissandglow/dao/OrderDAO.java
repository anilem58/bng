package com.blissandglow.dao;

import com.blissandglow.model.Order;
import com.blissandglow.model.OrderItem;
import com.blissandglow.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setOrderId(rs.getInt("order_id"));
        o.setUserId(rs.getInt("user_id"));
        Timestamp ts = rs.getTimestamp("order_date");
        if (ts != null) o.setOrderDate(ts.toLocalDateTime());
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setStatus(rs.getString("status"));
        o.setShippingAddress(rs.getString("shipping_address"));
        try { o.setCustomerName(rs.getString("customer_name")); } catch (SQLException ignore) {}
        return o;
    }

    private OrderItem mapItem(ResultSet rs) throws SQLException {
        OrderItem oi = new OrderItem();
        oi.setOrderItemId(rs.getInt("order_item_id"));
        oi.setOrderId(rs.getInt("order_id"));
        oi.setProductId(rs.getInt("product_id"));
        oi.setQuantity(rs.getInt("quantity"));
        oi.setUnitPrice(rs.getBigDecimal("unit_price"));
        try { oi.setProductName(rs.getString("product_name")); } catch (SQLException ignore) {}
        try { oi.setImagePath(rs.getString("image_path")); } catch (SQLException ignore) {}
        return oi;
    }

    /** Create an order and insert its items in one transaction. Returns the new order_id. */
    public int placeOrder(Order order) throws SQLException {
        String sqlOrder = "INSERT INTO orders (user_id, total_amount, status, shipping_address) VALUES (?,?,?,?)";
        String sqlItem  = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?,?,?,?)";
        String sqlStock = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE product_id=? AND stock_quantity >= ?";

        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                int orderId;
                try (PreparedStatement ps = c.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, order.getUserId());
                    ps.setBigDecimal(2, order.getTotalAmount());
                    ps.setString(3, "PENDING");
                    ps.setString(4, order.getShippingAddress());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        orderId = keys.next() ? keys.getInt(1) : -1;
                    }
                }
                for (OrderItem item : order.getItems()) {
                    try (PreparedStatement ps = c.prepareStatement(sqlItem)) {
                        ps.setInt(1, orderId);
                        ps.setInt(2, item.getProductId());
                        ps.setInt(3, item.getQuantity());
                        ps.setBigDecimal(4, item.getUnitPrice());
                        ps.executeUpdate();
                    }
                    try (PreparedStatement ps = c.prepareStatement(sqlStock)) {
                        ps.setInt(1, item.getQuantity());
                        ps.setInt(2, item.getProductId());
                        ps.setInt(3, item.getQuantity());
                        int rows = ps.executeUpdate();
                        if (rows == 0) throw new SQLException("Insufficient stock for product_id=" + item.getProductId());
                    }
                }
                c.commit();
                return orderId;
            } catch (SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public Order findById(int orderId) throws SQLException {
        String sql = "SELECT o.*, u.full_name AS customer_name FROM orders o " +
                     "JOIN users u ON o.user_id=u.user_id WHERE o.order_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Order order = mapOrder(rs);
                order.setItems(findItemsByOrderId(orderId));
                return order;
            }
        }
    }

    public List<Order> findByUserId(int userId) throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id=? ORDER BY order_date DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapOrder(rs));
            }
        }
        return list;
    }

    public List<Order> findAll() throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, u.full_name AS customer_name FROM orders o " +
                     "JOIN users u ON o.user_id=u.user_id ORDER BY o.order_date DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapOrder(rs));
        }
        return list;
    }

    public List<OrderItem> findItemsByOrderId(int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.*, p.name AS product_name, p.image_path FROM order_items oi " +
                     "JOIN products p ON oi.product_id=p.product_id WHERE oi.order_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) items.add(mapItem(rs));
            }
        }
        return items;
    }

    public void updateStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET status=? WHERE order_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }

    /** Cancel an order (only if PENDING) and restore stock. */
    public void cancelOrder(int orderId, int userId) throws SQLException {
        String sqlCheck  = "SELECT status FROM orders WHERE order_id=? AND user_id=?";
        String sqlUpdate = "UPDATE orders SET status='CANCELLED' WHERE order_id=?";
        String sqlStock  = "UPDATE products p " +
                           "JOIN order_items oi ON p.product_id=oi.product_id " +
                           "SET p.stock_quantity = p.stock_quantity + oi.quantity " +
                           "WHERE oi.order_id=?";
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                try (PreparedStatement ps = c.prepareStatement(sqlCheck)) {
                    ps.setInt(1, orderId);
                    ps.setInt(2, userId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next() || !"PENDING".equals(rs.getString("status")))
                            throw new SQLException("Order cannot be cancelled.");
                    }
                }
                try (PreparedStatement ps = c.prepareStatement(sqlUpdate)) {
                    ps.setInt(1, orderId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement(sqlStock)) {
                    ps.setInt(1, orderId);
                    ps.executeUpdate();
                }
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public int countOrders() throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /** Total revenue from DELIVERED orders. */
    public java.math.BigDecimal totalRevenue() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount),0) FROM orders WHERE status='DELIVERED'";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getBigDecimal(1) : java.math.BigDecimal.ZERO;
        }
    }
}
