package com.blissandglow.controller;

import com.blissandglow.model.User;
import com.blissandglow.service.OrderService;
import com.blissandglow.service.UserService;
import com.blissandglow.util.DateUtil;
import com.blissandglow.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

// Handles the customer dashboard, profile, and orders list
@WebServlet("/user/*")
public class UserServlet extends HttpServlet {

    private final UserService  userService  = new UserService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) path = "/dashboard";

        switch (path) {
            case "/dashboard" -> showDashboard(req, resp);
            case "/profile"   -> showProfile(req, resp);
            case "/orders"    -> showOrders(req, resp);
            default           -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) path = "";

        switch (path) {
            case "/profile"        -> handleUpdateProfile(req, resp);
            case "/changePassword" -> handleChangePassword(req, resp);
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // ── Pages ──────────────────────────────────────────────────────────

    private void showDashboard(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int userId = getLoggedInUserId(req);
            req.setAttribute("recentOrders", orderService.getOrdersByUser(userId));
            forward(req, resp, "/WEB-INF/views/user/dashboard.jsp");
        } catch (Exception e) { throw new ServletException(e); }
    }

    private void showProfile(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int userId = getLoggedInUserId(req);
            req.setAttribute("profileUser", userService.findById(userId));
            forward(req, resp, "/WEB-INF/views/user/profile.jsp");
        } catch (Exception e) { throw new ServletException(e); }
    }

    private void showOrders(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int userId = getLoggedInUserId(req);
            req.setAttribute("orders", orderService.getOrdersByUser(userId));
            forward(req, resp, "/WEB-INF/views/user/orders.jsp");
        } catch (Exception e) { throw new ServletException(e); }
    }

    // ── Actions ────────────────────────────────────────────────────────

    private void handleUpdateProfile(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            User loggedIn = SessionUtil.getUser(req.getSession());
            User updated  = new User();
            updated.setUserId(loggedIn.getUserId());
            updated.setFullName(req.getParameter("fullName"));
            updated.setPhone(req.getParameter("phone"));
            updated.setAddress(req.getParameter("address"));
            updated.setDob(DateUtil.parseInput(req.getParameter("dob")));

            String err = userService.updateProfile(updated);
            if (err != null) {
                req.setAttribute("error", err);
                req.setAttribute("profileUser", userService.findById(loggedIn.getUserId()));
                forward(req, resp, "/WEB-INF/views/user/profile.jsp");
                return;
            }

            // Refresh session with new info
            User refreshed = userService.findById(loggedIn.getUserId());
            SessionUtil.setUser(req.getSession(), refreshed);
            resp.sendRedirect(req.getContextPath() + "/user/profile?msg=updated");
        } catch (Exception e) { throw new ServletException(e); }
    }

    private void handleChangePassword(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int    userId  = getLoggedInUserId(req);
            String current = req.getParameter("currentPassword");
            String newPwd  = req.getParameter("newPassword");
            String confirm = req.getParameter("confirmPassword");

            // Passwords must match before we even try to change
            if (newPwd != null && !newPwd.equals(confirm)) {
                req.setAttribute("pwdError", "New passwords do not match.");
                req.setAttribute("profileUser", userService.findById(userId));
                forward(req, resp, "/WEB-INF/views/user/profile.jsp");
                return;
            }

            String err = userService.changePassword(userId, current, newPwd);
            if (err != null) {
                req.setAttribute("pwdError", err);
                req.setAttribute("profileUser", userService.findById(userId));
                forward(req, resp, "/WEB-INF/views/user/profile.jsp");
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/user/profile?msg=pwd_changed");
        } catch (Exception e) { throw new ServletException(e); }
    }

    // ── Helpers ────────────────────────────────────────────────────────

    private int getLoggedInUserId(HttpServletRequest req) {
        return SessionUtil.getUser(req.getSession()).getUserId();
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp, String path)
            throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, resp);
    }
}