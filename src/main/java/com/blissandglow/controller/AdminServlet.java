package com.blissandglow.servlet;

import com.blissandglow.dao.AdminDAO;
import com.blissandglow.model.Product;
import com.blissandglow.model.User;
import com.blissandglow.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * AdminServlet - Controller for all admin dashboard operations.
 *
 * URL mapping: /admin/*
 * Handles: dashboard, user management, product management
 *
 * Flow: AdminServlet → AdminDAO → MySQL
 *
 * Author: Member 5 (Admin + Security)
 */
@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {

    private AdminDAO adminDAO;

    @Override
    public void init() {
        adminDAO = new AdminDAO();
    }

    // ─── GET requests ─────────────────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Security check: only allow logged-in admins
        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login?error=unauthorized");
            return;
        }

        // Determine which admin page to load based on the URL path
        String pathInfo = request.getPathInfo(); // e.g. "/dashboard", "/users", "/products"
        if (pathInfo == null) pathInfo = "/dashboard";

        switch (pathInfo) {
            case "/dashboard":
                showDashboard(request, response);
                break;

            case "/users":
                showUserList(request, response);
                break;

            case "/users/edit":
                showEditUserForm(request, response);
                break;

            case "/products":
                showProductList(request, response);
                break;

            case "/products/add":
                showAddProductForm(request, response);
                break;

            case "/products/edit":
                showEditProductForm(request, response);
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
    }

    // ─── POST requests ────────────────────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Security check: only allow logged-in admins
        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login?error=unauthorized");
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "";

        switch (pathInfo) {
            case "/users/update":
                handleUpdateUser(request, response);
                break;

            case "/users/delete":
                handleDeleteUser(request, response);
                break;

            case "/products/add":
                handleAddProduct(request, response);
                break;

            case "/products/update":
                handleUpdateProduct(request, response);
                break;

            case "/products/delete":
                handleDeleteProduct(request, response);
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
    }

    // ═══════════════════════════════════════════════════════
    //  GET HANDLERS
    // ═══════════════════════════════════════════════════════

    /**
     * Loads summary statistics and forwards to the admin dashboard page.
     */
    private void showDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("totalUsers", adminDAO.getTotalUsers());
        request.setAttribute("totalProducts", adminDAO.getTotalProducts());
        request.setAttribute("totalOrders", adminDAO.getTotalOrders());
        request.setAttribute("totalRevenue", adminDAO.getTotalRevenue());

        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp")
                .forward(request, response);
    }

    /**
     * Loads all users and forwards to the user management page.
     */
    private void showUserList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<User> users = adminDAO.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/WEB-INF/views/admin/manage-users.jsp")
                .forward(request, response);
    }

    /**
     * Loads a single user and forwards to the edit user form.
     */
    private void showEditUserForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userId = parseId(request.getParameter("id"));
        if (userId <= 0) {
            response.sendRedirect(request.getContextPath() + "/admin/users?error=invalid_id");
            return;
        }

        User user = adminDAO.getUserById(userId);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/admin/users?error=not_found");
            return;
        }

        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/views/admin/edit-user.jsp")
                .forward(request, response);
    }

    /**
     * Loads all products and forwards to the product management page.
     */
    private void showProductList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Product> products = adminDAO.getAllProducts();
        request.setAttribute("products", products);
        request.getRequestDispatcher("/WEB-INF/views/admin/manage-products.jsp")
                .forward(request, response);
    }

    /**
     * Forwards to the add new product form.
     */
    private void showAddProductForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/admin/add-product.jsp")
                .forward(request, response);
    }

    /**
     * Loads a single product and forwards to the edit product form.
     */
    private void showEditProductForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int productId = parseId(request.getParameter("id"));
        if (productId <= 0) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=invalid_id");
            return;
        }

        Product product = adminDAO.getProductById(productId);
        if (product == null) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=not_found");
            return;
        }

        request.setAttribute("product", product);
        request.getRequestDispatcher("/WEB-INF/views/admin/edit-product.jsp")
                .forward(request, response);
    }

    // ═══════════════════════════════════════════════════════
    //  POST HANDLERS
    // ═══════════════════════════════════════════════════════

    /**
     * Handles updating a user's role and status from the admin panel.
     */
    private void handleUpdateUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int userId  = parseId(request.getParameter("userId"));
        String role   = ValidationUtil.sanitize(request.getParameter("role"));
        String status = ValidationUtil.sanitize(request.getParameter("status"));

        // Validate inputs
        String error = ValidationUtil.validateUserForm(
                request.getParameter("username"),
                request.getParameter("email"),
                request.getParameter("fullName"),
                role
        );

        if (error != null || !ValidationUtil.isValidStatus(status)) {
            response.sendRedirect(request.getContextPath() +
                    "/admin/users/edit?id=" + userId + "&error=" + encode(error != null ? error : "Invalid status"));
            return;
        }

        boolean success = adminDAO.updateUser(userId, role, status);
        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/users?success=user_updated");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/users?error=update_failed");
        }
    }

    /**
     * Handles deleting a user from the admin panel.
     */
    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int userId = parseId(request.getParameter("userId"));
        if (userId <= 0) {
            response.sendRedirect(request.getContextPath() + "/admin/users?error=invalid_id");
            return;
        }

        boolean success = adminDAO.deleteUser(userId);
        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/users?success=user_deleted");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/users?error=delete_failed");
        }
    }

    /**
     * Handles adding a new product from the admin panel.
     */
    private void handleAddProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String name        = ValidationUtil.sanitize(request.getParameter("name"));
        String description = ValidationUtil.sanitize(request.getParameter("description"));
        String price       = ValidationUtil.sanitize(request.getParameter("price"));
        String stock       = ValidationUtil.sanitize(request.getParameter("stock"));
        String category    = ValidationUtil.sanitize(request.getParameter("category"));
        String imageUrl    = ValidationUtil.sanitize(request.getParameter("imageUrl"));

        // Validate all product fields
        String error = ValidationUtil.validateProductForm(name, description, price, stock, category);
        if (error != null) {
            response.sendRedirect(request.getContextPath() +
                    "/admin/products/add?error=" + encode(error));
            return;
        }

        // Build product model and persist
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(Double.parseDouble(price));
        product.setStock(Integer.parseInt(stock));
        product.setCategory(category);
        product.setImageUrl(imageUrl.isEmpty() ? "default-product.jpg" : imageUrl);

        boolean success = adminDAO.addProduct(product);
        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/products?success=product_added");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/products/add?error=add_failed");
        }
    }

    /**
     * Handles updating an existing product from the admin panel.
     */
    private void handleUpdateProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int productId  = parseId(request.getParameter("productId"));
        String name        = ValidationUtil.sanitize(request.getParameter("name"));
        String description = ValidationUtil.sanitize(request.getParameter("description"));
        String price       = ValidationUtil.sanitize(request.getParameter("price"));
        String stock       = ValidationUtil.sanitize(request.getParameter("stock"));
        String category    = ValidationUtil.sanitize(request.getParameter("category"));
        String imageUrl    = ValidationUtil.sanitize(request.getParameter("imageUrl"));

        String error = ValidationUtil.validateProductForm(name, description, price, stock, category);
        if (error != null) {
            response.sendRedirect(request.getContextPath() +
                    "/admin/products/edit?id=" + productId + "&error=" + encode(error));
            return;
        }

        Product product = new Product();
        product.setProductId(productId);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(Double.parseDouble(price));
        product.setStock(Integer.parseInt(stock));
        product.setCategory(category);
        product.setImageUrl(imageUrl.isEmpty() ? "default-product.jpg" : imageUrl);

        boolean success = adminDAO.updateProduct(product);
        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/products?success=product_updated");
        } else {
            response.sendRedirect(request.getContextPath() +
                    "/admin/products/edit?id=" + productId + "&error=update_failed");
        }
    }

    /**
     * Handles deleting a product from the admin panel.
     */
    private void handleDeleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int productId = parseId(request.getParameter("productId"));
        if (productId <= 0) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=invalid_id");
            return;
        }

        boolean success = adminDAO.deleteProduct(productId);
        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/products?success=product_deleted");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=delete_failed");
        }
    }

    // ═══════════════════════════════════════════════════════
    //  SECURITY & HELPERS
    // ═══════════════════════════════════════════════════════

    /**
     * Checks if the current session belongs to a logged-in admin user.
     * If not authenticated or not an admin, returns false.
     *
     * @param request the HTTP request
     * @return true if admin session is valid
     */
    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;
        String role = (String) session.getAttribute("role");
        return "admin".equalsIgnoreCase(role);
    }

    /**
     * Safely parses an integer ID from a string parameter.
     * Returns -1 if parsing fails.
     */
    private int parseId(String param) {
        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException | NullPointerException e) {
            return -1;
        }
    }

    /**
     * URL-encodes a string for safe use in redirect query parameters.
     */
    private String encode(String value) {
        try {
            return java.net.URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            return value;
        }
    }
}
