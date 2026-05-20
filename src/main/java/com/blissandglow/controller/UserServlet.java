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
            case "/profile"         -> handleUpdateProfile(req, resp);
            case "/changePassword"  -> handleChangePassword(req, resp);
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showDashboard(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int userId = SessionUtil.getUser(req.getSession()).getUserId();
            req.setAttribute("recentOrders", orderService.getOrdersByUser(userId));
            req.getRequestDispatcher("/WEB-INF/views/user/dashboard.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    private void showProfile(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int userId = SessionUtil.getUser(req.getSession()).getUserId();
            req.setAttribute("profileUser", userService.findById(userId));
            req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    private void showOrders(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int userId = SessionUtil.getUser(req.getSession()).getUserId();
            req.setAttribute("orders", orderService.getOrdersByUser(userId));
            req.getRequestDispatcher("/WEB-INF/views/user/orders.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    private void handleUpdateProfile(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            User loggedIn = SessionUtil.getUser(req.getSession());
            User user = new User();
            user.setUserId(loggedIn.getUserId());
            user.setFullName(req.getParameter("fullName"));
            user.setPhone(req.getParameter("phone"));
            user.setAddress(req.getParameter("address"));
            user.setDob(DateUtil.parseInput(req.getParameter("dob")));

            String err = userService.updateProfile(user);
            if (err != null) {
                req.setAttribute("error", err);
                req.setAttribute("profileUser", userService.findById(loggedIn.getUserId()));
                req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
                return;
            }
            User updated = userService.findById(loggedIn.getUserId());
            SessionUtil.setUser(req.getSession(), updated);
            resp.sendRedirect(req.getContextPath() + "/user/profile?msg=updated");
        } catch (Exception e) { throw new ServletException(e); }
    }

    private void handleChangePassword(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int userId      = SessionUtil.getUser(req.getSession()).getUserId();
            String current  = req.getParameter("currentPassword");
            String newPwd   = req.getParameter("newPassword");
            String confirm  = req.getParameter("confirmPassword");
            if (newPwd != null && !newPwd.equals(confirm)) {
                req.setAttribute("pwdError", "New passwords do not match.");
                req.setAttribute("profileUser", userService.findById(userId));
                req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
                return;
            }
            String err = userService.changePassword(userId, current, newPwd);
            if (err != null) {
                req.setAttribute("pwdError", err);
                req.setAttribute("profileUser", userService.findById(userId));
                req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/user/profile?msg=pwd_changed");
        } catch (Exception e) { throw new ServletException(e); }
    }
}
