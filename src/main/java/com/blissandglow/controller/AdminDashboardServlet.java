package com.blissandglow.controller;

import com.blissandglow.dao.ContactDAO;
import com.blissandglow.model.Category;
import com.blissandglow.model.Product;
import com.blissandglow.service.OrderService;
import com.blissandglow.service.ProductService;
import com.blissandglow.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.UUID;

@WebServlet("/admin/*")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class AdminDashboardServlet extends HttpServlet {

    private final UserService    userService    = new UserService();
    private final ProductService productService = new ProductService();
    private final OrderService   orderService   = new OrderService();
    private final ContactDAO     contactDAO     = new ContactDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) path = "/dashboard";
        switch (path) {
            case "/dashboard"         -> showDashboard(req, resp);
            case "/products"          -> showProducts(req, resp);
            case "/products/add"      -> showAddProduct(req, resp);
            case "/products/edit"     -> showEditProduct(req, resp);
            case "/categories"        -> showCategories(req, resp);
            case "/users"             -> showUsers(req, resp);
            case "/orders"            -> showOrders(req, resp);
            case "/orders/detail"     -> showOrderDetail(req, resp);
            case "/messages"          -> showMessages(req, resp);
            case "/reports"           -> showReports(req, resp);
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) path = "";
        switch (path) {
            case "/products/add"      -> handleAddProduct(req, resp);
            case "/products/edit"     -> handleEditProduct(req, resp);
            case "/products/delete"   -> handleDeleteProduct(req, resp);
            case "/categories/add"    -> handleAddCategory(req, resp);
            case "/categories/edit"   -> handleEditCategory(req, resp);
            case "/categories/delete" -> handleDeleteCategory(req, resp);
            case "/users/approve"     -> handleApproveUser(req, resp);
            case "/users/reject"      -> handleRejectUser(req, resp);
            case "/orders/status"     -> handleOrderStatus(req, resp);
            case "/messages/read"     -> handleMarkRead(req, resp);
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // ── Dashboard ────────────────────────────────────────────────────────

    private void showDashboard(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("totalUsers",    userService.countCustomers());
            req.setAttribute("totalProducts", productService.countProducts());
            req.setAttribute("totalOrders",   orderService.countOrders());
            req.setAttribute("pendingUsers",  userService.countPending());
            req.setAttribute("unreadMessages", contactDAO.countUnread());
            req.setAttribute("recentOrders",  orderService.getAllOrders());
            req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    // ── Products ─────────────────────────────────────────────────────────

    private void showProducts(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("products", productService.getAll());
            req.getRequestDispatcher("/WEB-INF/views/admin/manageProducts.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    private void showAddProduct(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("categories", productService.getAllCategories());
            req.getRequestDispatcher("/WEB-INF/views/admin/addProduct.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    private void showEditProduct(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("product",    productService.getById(id));
            req.setAttribute("categories", productService.getAllCategories());
            req.getRequestDispatcher("/WEB-INF/views/admin/editProduct.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    private void handleAddProduct(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Product p = buildProductFromRequest(req);
            Part imagePart = req.getPart("image");
            if (imagePart != null && imagePart.getSize() > 0) {
                p.setImagePath(saveImage(req, imagePart));
            }
            String err = productService.addProduct(p);
            if (err != null) {
                req.setAttribute("error", err);
                req.setAttribute("categories", productService.getAllCategories());
                req.getRequestDispatcher("/WEB-INF/views/admin/addProduct.jsp").forward(req, resp);
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/admin/products?msg=added");
        } catch (Exception e) { throw new ServletException(e); }
    }

    private void handleEditProduct(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Product p = buildProductFromRequest(req);
            p.setProductId(Integer.parseInt(req.getParameter("productId")));
            Part imagePart = req.getPart("image");
            if (imagePart != null && imagePart.getSize() > 0) {
                p.setImagePath(saveImage(req, imagePart));
            } else {
                p.setImagePath(req.getParameter("existingImage"));
            }
            String err = productService.updateProduct(p);
            if (err != null) {
                req.setAttribute("error", err);
                req.setAttribute("product",    productService.getById(p.getProductId()));
                req.setAttribute("categories", productService.getAllCategories());
                req.getRequestDispatcher("/WEB-INF/views/admin/editProduct.jsp").forward(req, resp);
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/admin/products?msg=updated");
        } catch (Exception e) { throw new ServletException(e); }
    }

    private void handleDeleteProduct(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("productId"));
            productService.deleteProduct(id);
            resp.sendRedirect(req.getContextPath() + "/admin/products?msg=deleted");
        } catch (Exception e) { throw new ServletException(e); }
    }

    private Product buildProductFromRequest(HttpServletRequest req) {
        Product p = new Product();
        p.setName(req.getParameter("name"));
        p.setBrand(req.getParameter("brand"));
        p.setDescription(req.getParameter("description"));
        p.setSku(req.getParameter("sku"));
        p.setCategoryId(Integer.parseInt(req.getParameter("categoryId")));
        try { p.setPrice(new BigDecimal(req.getParameter("price"))); } catch (Exception e) { p.setPrice(BigDecimal.ZERO); }
        try { p.setStockQuantity(Integer.parseInt(req.getParameter("stockQuantity"))); } catch (Exception e) { p.setStockQuantity(0); }
        return p;
    }

    private String saveImage(HttpServletRequest req, Part part) throws IOException {
        String uploadsDir = req.getServletContext().getRealPath("/assets/images/products");
        new File(uploadsDir).mkdirs();
        String ext       = getExtension(part.getSubmittedFileName());
        String filename  = UUID.randomUUID().toString() + ext;
        part.write(uploadsDir + File.separator + filename);
        return "/assets/images/products/" + filename;
    }

    private String getExtension(String filename) {
        if (filename == null) return ".jpg";
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot) : ".jpg";
    }

    // ── Categories ───────────────────────────────────────────────────────

    private void showCategories(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("categories", productService.getAllCategories());
            req.getRequestDispatcher("/WEB-INF/views/admin/manageCategories.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    private void handleAddCategory(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Category cat = new Category();
            cat.setName(req.getParameter("name"));
            cat.setDescription(req.getParameter("description"));
            String err = productService.addCategory(cat);
            if (err != null) req.getSession().setAttribute("flashError", err);
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
        } catch (Exception e) { throw new ServletException(e); }
    }

    private void handleEditCategory(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Category cat = new Category();
            cat.setCategoryId(Integer.parseInt(req.getParameter("categoryId")));
            cat.setName(req.getParameter("name"));
            cat.setDescription(req.getParameter("description"));
            String err = productService.updateCategory(cat);
            if (err != null) req.getSession().setAttribute("flashError", err);
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
        } catch (Exception e) { throw new ServletException(e); }
    }

    private void handleDeleteCategory(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("categoryId"));
            productService.deleteCategory(id);
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
        } catch (Exception e) { throw new ServletException(e); }
    }

    // ── Users ────────────────────────────────────────────────────────────

    private void showUsers(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("users", userService.getAllUsers());
            req.getRequestDispatcher("/WEB-INF/views/admin/manageUsers.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    private void handleApproveUser(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            userService.approveUser(Integer.parseInt(req.getParameter("userId")));
            resp.sendRedirect(req.getContextPath() + "/admin/users?msg=approved");
        } catch (Exception e) { throw new ServletException(e); }
    }

    private void handleRejectUser(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            userService.rejectUser(Integer.parseInt(req.getParameter("userId")));
            resp.sendRedirect(req.getContextPath() + "/admin/users?msg=rejected");
        } catch (Exception e) { throw new ServletException(e); }
    }

    // ── Orders ───────────────────────────────────────────────────────────

    private void showOrders(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("orders", orderService.getAllOrders());
            req.getRequestDispatcher("/WEB-INF/views/admin/manageOrders.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    private void showOrderDetail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("order", orderService.getOrderById(id));
            req.getRequestDispatcher("/WEB-INF/views/admin/orderDetail.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    private void handleOrderStatus(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(req.getParameter("orderId"));
            orderService.updateStatus(orderId, req.getParameter("status"));
            resp.sendRedirect(req.getContextPath() + "/admin/orders?msg=updated");
        } catch (Exception e) { throw new ServletException(e); }
    }

    // ── Contact messages ─────────────────────────────────────────────────

    private void showMessages(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("messages", contactDAO.findAll());
            req.getRequestDispatcher("/WEB-INF/views/admin/contactMessages.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    private void handleMarkRead(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            contactDAO.markAsRead(Integer.parseInt(req.getParameter("messageId")));
            resp.sendRedirect(req.getContextPath() + "/admin/messages");
        } catch (Exception e) { throw new ServletException(e); }
    }

    // ── Reports ──────────────────────────────────────────────────────────

    private void showReports(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("topProducts",    productService.getTopOrderedProducts(5));
            req.setAttribute("salesByCategory", productService.getSalesByCategory());
            req.setAttribute("stockReport",    productService.getStockReport());
            req.setAttribute("totalRevenue",   orderService.totalRevenue());
            req.getRequestDispatcher("/WEB-INF/views/admin/reports.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }
}
