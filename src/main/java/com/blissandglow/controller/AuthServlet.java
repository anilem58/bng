package com.blissandglow.controller;

import com.blissandglow.model.User;
import com.blissandglow.service.UserService;
import com.blissandglow.util.CookieUtil;
import com.blissandglow.util.DateUtil;
import com.blissandglow.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

// Handles login, logout, and registration
@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "loginForm";

        switch (action) {
            case "loginForm"    -> forward(req, resp, "/WEB-INF/views/auth/login.jsp");
            case "registerForm" -> forward(req, resp, "/WEB-INF/views/auth/register.jsp");
            case "logout"       -> handleLogout(req, resp);
            default             -> forward(req, resp, "/WEB-INF/views/auth/login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("login".equals(action)) {
            handleLogin(req, resp);
        } else if ("register".equals(action)) {
            handleRegister(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/auth?action=loginForm");
        }
    }

    // ── Login ──────────────────────────────────────────────────────────

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email    = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User user = userService.login(email, password);

            // User not found or wrong password
            if (user == null) {
                req.setAttribute("error", "Invalid email or password.");
                forward(req, resp, "/WEB-INF/views/auth/login.jsp");
                return;
            }

            // Account not yet approved by admin
            if ("PENDING".equals(user.getStatus())) {
                req.setAttribute("error", "Your account is awaiting admin approval.");
                forward(req, resp, "/WEB-INF/views/auth/login.jsp");
                return;
            }

            // Account was rejected
            if ("REJECTED".equals(user.getStatus())) {
                req.setAttribute("error", "Your account registration was rejected.");
                forward(req, resp, "/WEB-INF/views/auth/login.jsp");
                return;
            }

            // Save user in session
            HttpSession session = req.getSession(true);
            SessionUtil.setUser(session, user);

            // If "Remember me" is checked, set a cookie for 7 days
            if ("on".equals(req.getParameter("rememberMe"))) {
                CookieUtil.addCookie(resp, "rememberMe", String.valueOf(user.getUserId()), 7 * 24 * 60 * 60);
            }

            // Send to the right dashboard based on role
            String redirect = "ADMIN".equals(user.getRole())
                ? req.getContextPath() + "/admin/dashboard"
                : req.getContextPath() + "/user/dashboard";
            resp.sendRedirect(redirect);

        } catch (Exception e) {
            req.setAttribute("error", "A system error occurred. Please try again.");
            forward(req, resp, "/WEB-INF/views/auth/login.jsp");
        }
    }

    // ── Register ───────────────────────────────────────────────────────

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Collect all registration fields
        User user = new User();
        user.setFullName(req.getParameter("fullName"));
        user.setEmail(req.getParameter("email") != null
            ? req.getParameter("email").trim().toLowerCase() : "");
        user.setPhone(req.getParameter("phone"));
        user.setAddress(req.getParameter("address"));
        user.setDob(DateUtil.parseInput(req.getParameter("dob")));

        String password = req.getParameter("password");
        String confirm  = req.getParameter("confirmPassword");

        // Check passwords match before anything else
        if (password != null && !password.equals(confirm)) {
            req.setAttribute("error", "Passwords do not match.");
            req.setAttribute("user", user);
            forward(req, resp, "/WEB-INF/views/auth/register.jsp");
            return;
        }

        try {
            String err = userService.register(user, password);
            if (err != null) {
                // Validation failed — show error and keep form filled
                req.setAttribute("error", err);
                req.setAttribute("user", user);
                forward(req, resp, "/WEB-INF/views/auth/register.jsp");
                return;
            }
            // Registration successful — go to login page with success message
            resp.sendRedirect(req.getContextPath() + "/auth?action=loginForm&msg=registered");
        } catch (Exception e) {
            req.setAttribute("error", "A system error occurred. Please try again.");
            req.setAttribute("user", user);
            forward(req, resp, "/WEB-INF/views/auth/register.jsp");
        }
    }

    // ── Logout ─────────────────────────────────────────────────────────

    private void handleLogout(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        SessionUtil.invalidate(req.getSession(false));
        CookieUtil.deleteCookie(resp, "rememberMe");
        resp.sendRedirect(req.getContextPath() + "/auth?action=loginForm&msg=logged_out");
    }

    // ── Helper ─────────────────────────────────────────────────────────

    private void forward(HttpServletRequest req, HttpServletResponse resp, String path)
            throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, resp);
    }
}