package com.blissandglow.filter;

import com.blissandglow.model.User;
import com.blissandglow.util.SessionUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

// This filter protects all /admin/* and /user/* pages
// Anyone not logged in gets redirected to the login page
@WebFilter({"/admin/*", "/user/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest)  req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Check if there is a logged-in user in the session
        HttpSession session = request.getSession(false);
        User user = (session != null) ? SessionUtil.getUser(session) : null;
        String uri = request.getRequestURI();

        // Not logged in — send to login page
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=loginForm&error=login_required");
            return;
        }

        // Customer trying to access admin pages — show access denied
        if (uri.contains("/admin/") && !"ADMIN".equals(user.getRole())) {
            request.getRequestDispatcher("/WEB-INF/views/error/accessDenied.jsp").forward(request, response);
            return;
        }

        // Admin trying to access customer pages — show access denied
        if (uri.contains("/user/") && !"CUSTOMER".equals(user.getRole())) {
            request.getRequestDispatcher("/WEB-INF/views/error/accessDenied.jsp").forward(request, response);
            return;
        }

        // All checks passed — let the request continue
        chain.doFilter(req, res);
    }
}