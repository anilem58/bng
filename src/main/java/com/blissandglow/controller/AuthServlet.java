package com.blissandglow.controller;

import com.blissandglow.model.User;
import com.blissandglow.service.UserService;
import com.blissandglow.util.DateUtil;
import com.blissandglow.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

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
            case "logout"       -> {
                SessionUtil.invalidate(req.getSession(false));
                Cookie remember = new Cookie("rememberMe", "");
                remember.setMaxAge(0);
                remember.setPath("/");
                resp.addCookie(remember);
                resp.sendRedirect(req.getContextPath() + "/auth?action=loginForm&msg=logged_out");
            }
            default -> forward(req, resp, "/WEB-INF/views/auth/login.jsp");
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

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email    = req.getParameter("email");
        String password = req.getParameter("password");
        try {
            User user = userService.login(email, password);
            if (user == null) {
                req.setAttribute("error", "Invalid email or password.");
                forward(req, resp, "/WEB-INF/views/auth/login.jsp");
                return;
            }
            if ("PENDING".equals(user.getStatus())) {
                req.setAttribute("error", "Your account is awaiting admin approval.");
                forward(req, resp, "/WEB-INF/views/auth/login.jsp");
                return;
            }
            if ("REJECTED".equals(user.getStatus())) {
                req.setAttribute("error", "Your account registration was rejected.");
                forward(req, resp, "/WEB-INF/views/auth/login.jsp");
                return;
            }
            HttpSession session = req.getSession(true);
            SessionUtil.setUser(session, user);

            if ("on".equals(req.getParameter("rememberMe"))) {
                Cookie c = new Cookie("rememberMe", String.valueOf(user.getUserId()));
                c.setMaxAge(7 * 24 * 60 * 60);
                c.setPath("/");
                c.setHttpOnly(true);
                resp.addCookie(c);
            }

            String redirect = "ADMIN".equals(user.getRole())
                ? req.getContextPath() + "/admin/dashboard"
                : req.getContextPath() + "/user/dashboard";
            resp.sendRedirect(redirect);
        } catch (Exception e) {
            req.setAttribute("error", "A system error occurred. Please try again.");
            forward(req, resp, "/WEB-INF/views/auth/login.jsp");
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = new User();
        user.setFullName(req.getParameter("fullName"));
        user.setEmail(req.getParameter("email") != null ? req.getParameter("email").trim().toLowerCase() : "");
        user.setPhone(req.getParameter("phone"));
        user.setAddress(req.getParameter("address"));
        LocalDate dob = DateUtil.parseInput(req.getParameter("dob"));
        user.setDob(dob);
        String password = req.getParameter("password");
        String confirm  = req.getParameter("confirmPassword");
        if (password != null && !password.equals(confirm)) {
            req.setAttribute("error", "Passwords do not match.");
            req.setAttribute("user", user);
            forward(req, resp, "/WEB-INF/views/auth/register.jsp");
            return;
        }
        try {
            String err = userService.register(user, password);
            if (err != null) {
                req.setAttribute("error", err);
                req.setAttribute("user", user);
                forward(req, resp, "/WEB-INF/views/auth/register.jsp");
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/auth?action=loginForm&msg=registered");
        } catch (Exception e) {
            req.setAttribute("error", "A system error occurred. Please try again.");
            req.setAttribute("user", user);
            forward(req, resp, "/WEB-INF/views/auth/register.jsp");
        }
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp, String path)
            throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, resp);
    }
}
