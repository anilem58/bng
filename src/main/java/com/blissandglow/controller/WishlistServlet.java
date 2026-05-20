package com.blissandglow.controller;

import com.blissandglow.service.WishlistService;
import com.blissandglow.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/user/wishlist")
public class WishlistServlet extends HttpServlet {

    private final WishlistService wishlistService = new WishlistService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int userId = SessionUtil.getUser(req.getSession()).getUserId();
            req.setAttribute("wishlistItems", wishlistService.getWishlist(userId));
            req.getRequestDispatcher("/WEB-INF/views/user/wishlist.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        int productId;
        try { productId = Integer.parseInt(req.getParameter("productId")); }
        catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/user/wishlist");
            return;
        }
        int userId = SessionUtil.getUser(req.getSession()).getUserId();
        try {
            if ("remove".equals(action)) {
                wishlistService.remove(userId, productId);
            } else {
                wishlistService.toggle(userId, productId);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
        String ref = req.getHeader("Referer");
        resp.sendRedirect(ref != null ? ref : req.getContextPath() + "/user/wishlist");
    }
}
