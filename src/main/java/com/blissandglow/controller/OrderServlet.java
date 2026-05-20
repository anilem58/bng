package com.blissandglow.controller;

import com.blissandglow.model.Order;
import com.blissandglow.model.OrderItem;
import com.blissandglow.model.Product;
import com.blissandglow.service.OrderService;
import com.blissandglow.service.ProductService;
import com.blissandglow.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Handles the shopping cart and order placement/cancellation
@WebServlet("/user/order")
public class OrderServlet extends HttpServlet {

    private final OrderService   orderService   = new OrderService();
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("detail".equals(action)) {
            showOrderDetail(req, resp);
        } else if ("cart".equals(action)) {
            forward(req, resp, "/WEB-INF/views/user/cart.jsp");
        } else {
            resp.sendRedirect(req.getContextPath() + "/user/orders");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action == null ? "" : action) {
            case "addToCart"  -> addToCart(req, resp);
            case "removeCart" -> removeFromCart(req, resp);
            case "checkout"   -> checkout(req, resp);
            case "cancel"     -> cancelOrder(req, resp);
            default -> resp.sendRedirect(req.getContextPath() + "/user/orders");
        }
    }

    // ── Pages ──────────────────────────────────────────────────────────

    private void showOrderDetail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(req.getParameter("id"));
            int userId  = getLoggedInUserId(req);
            Order order = orderService.getOrderById(orderId);

            // Make sure the order belongs to this customer
            if (order == null || order.getUserId() != userId) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            req.setAttribute("order", order);
            forward(req, resp, "/WEB-INF/views/user/orderDetail.jsp");
        } catch (Exception e) { throw new ServletException(e); }
    }

    // ── Cart Actions ───────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private void addToCart(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(req.getParameter("productId"));
            int qty       = parseQty(req.getParameter("qty"));

            Product product = productService.getById(productId);

            // Check the product exists and has enough stock
            if (product == null || product.getStockQuantity() < qty) {
                resp.sendRedirect(req.getContextPath() + "/products?action=detail&id=" + productId);
                return;
            }

            // Get or create the cart in the session
            HttpSession session = req.getSession(true);
            List<OrderItem> cart = (List<OrderItem>) session.getAttribute("cart");
            if (cart == null) cart = new ArrayList<>();

            // If item already in cart, increase quantity
            boolean found = false;
            for (OrderItem item : cart) {
                if (item.getProductId() == productId) {
                    item.setQuantity(item.getQuantity() + qty);
                    found = true;
                    break;
                }
            }

            // Otherwise add as a new item
            if (!found) {
                OrderItem item = new OrderItem();
                item.setProductId(productId);
                item.setProductName(product.getName());
                item.setImagePath(product.getImagePath());
                item.setUnitPrice(product.getPrice());
                item.setQuantity(qty);
                cart.add(item);
            }

            session.setAttribute("cart", cart);
            resp.sendRedirect(req.getContextPath() + "/user/order?action=cart");
        } catch (Exception e) { throw new ServletException(e); }
    }

    @SuppressWarnings("unchecked")
    private void removeFromCart(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int productId;
        try { productId = Integer.parseInt(req.getParameter("productId")); }
        catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/user/order?action=cart");
            return;
        }

        HttpSession session = req.getSession(false);
        if (session != null) {
            List<OrderItem> cart = (List<OrderItem>) session.getAttribute("cart");
            if (cart != null) cart.removeIf(i -> i.getProductId() == productId);
        }
        resp.sendRedirect(req.getContextPath() + "/user/order?action=cart");
    }

    @SuppressWarnings("unchecked")
    private void checkout(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            HttpSession session = req.getSession(false);
            List<OrderItem> cart = session != null
                ? (List<OrderItem>) session.getAttribute("cart") : null;

            if (cart == null || cart.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/user/order?action=cart");
                return;
            }

            String address = req.getParameter("shippingAddress");
            if (address == null || address.isBlank()) {
                req.setAttribute("cartError", "Please provide a shipping address.");
                forward(req, resp, "/WEB-INF/views/user/cart.jsp");
                return;
            }

            int userId  = getLoggedInUserId(req);
            int orderId = orderService.placeOrder(userId, cart, address);

            // Clear the cart after a successful order
            session.removeAttribute("cart");
            resp.sendRedirect(req.getContextPath() + "/user/order?action=detail&id=" + orderId + "&msg=placed");
        } catch (Exception e) { throw new ServletException(e); }
    }

    private void cancelOrder(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(req.getParameter("orderId"));
            int userId  = getLoggedInUserId(req);
            orderService.cancelOrder(orderId, userId);
            resp.sendRedirect(req.getContextPath() + "/user/orders?msg=cancelled");
        } catch (Exception e) { throw new ServletException(e); }
    }

    // ── Helpers ────────────────────────────────────────────────────────

    private int getLoggedInUserId(HttpServletRequest req) {
        return SessionUtil.getUser(req.getSession()).getUserId();
    }

    private int parseQty(String value) {
        try {
            int q = Integer.parseInt(value);
            return q < 1 ? 1 : q;
        } catch (Exception e) { return 1; }
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp, String path)
            throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, resp);
    }
}