package com.blissandglow.filter;

import com.blissandglow.model.User;
import com.blissandglow.util.SessionUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter({"/admin/*", "/user/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest  request  = (HttpServletRequest)  req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);
        User user = session != null ? SessionUtil.getUser(session) : null;
        String uri = request.getRequestURI();

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=loginForm&error=login_required");
            return;
        }

        if (uri.contains("/admin/") && !"ADMIN".equals(user.getRole())) {
            request.getRequestDispatcher("/WEB-INF/views/error/accessDenied.jsp").forward(request, response);
            return;
        }

        if (uri.contains("/user/") && !"CUSTOMER".equals(user.getRole())) {
            request.getRequestDispatcher("/WEB-INF/views/error/accessDenied.jsp").forward(request, response);
            return;
        }

        chain.doFilter(req, res);
    }
}
