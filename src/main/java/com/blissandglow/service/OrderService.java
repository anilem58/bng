package com.blissandglow.service;

import com.blissandglow.dao.OrderDAO;
import com.blissandglow.model.Order;
import com.blissandglow.model.OrderItem;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class OrderService {

    private final OrderDAO orderDAO = new OrderDAO();

    /** Place a new order. Returns the new order_id. */
    public int placeOrder(int userId, List<OrderItem> items, String shippingAddress) throws SQLException {
        if (items == null || items.isEmpty()) throw new IllegalArgumentException("Cart is empty.");
        if (shippingAddress == null || shippingAddress.isBlank()) throw new IllegalArgumentException("Shipping address is required.");

        BigDecimal total = items.stream()
            .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(total);
        order.setShippingAddress(shippingAddress);
        order.setItems(items);
        return orderDAO.placeOrder(order);
    }

    public Order getOrderById(int orderId) throws SQLException {
        return orderDAO.findById(orderId);
    }

    public List<Order> getOrdersByUser(int userId) throws SQLException {
        return orderDAO.findByUserId(userId);
    }

    public List<Order> getAllOrders() throws SQLException {
        return orderDAO.findAll();
    }

    public void updateStatus(int orderId, String status) throws SQLException {
        String[] valid = {"PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"};
        boolean ok = false;
        for (String v : valid) if (v.equals(status)) { ok = true; break; }
        if (!ok) throw new IllegalArgumentException("Invalid status: " + status);
        orderDAO.updateStatus(orderId, status);
    }

    public void cancelOrder(int orderId, int userId) throws SQLException {
        orderDAO.cancelOrder(orderId, userId);
    }

    public int countOrders() throws SQLException { return orderDAO.countOrders(); }

    public BigDecimal totalRevenue() throws SQLException { return orderDAO.totalRevenue(); }
}
