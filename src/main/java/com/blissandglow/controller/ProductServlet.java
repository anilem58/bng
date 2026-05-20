package com.blissandglow.controller;

import com.blissandglow.model.Product;
import com.blissandglow.service.ProductService;
import com.blissandglow.service.WishlistService;
import com.blissandglow.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private final ProductService  productService  = new ProductService();
    private final WishlistService wishlistService = new WishlistService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("detail".equals(action)) {
            showDetail(req, resp);
        } else if ("search".equals(action)) {
            showSearch(req, resp);
        } else {
            showList(req, resp);
        }
    }

    // Skincare: Cleansers(1), Moisturizers(2), Serums(3), Sunscreen(4), Body Care(7)
    private static final int[] SKINCARE_IDS = {1, 2, 3, 4, 7};
    // Makeup: Lip Care(5), Makeup(6)
    private static final int[] MAKEUP_IDS   = {5, 6};
    // Haircare: Haircare(8)
    private static final int[] HAIRCARE_IDS = {8};

    private void showList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("skincareProducts", productService.getByCategories(SKINCARE_IDS));
            req.setAttribute("makeupProducts",   productService.getByCategories(MAKEUP_IDS));
            req.setAttribute("haircareProducts", productService.getByCategories(HAIRCARE_IDS));
            req.getRequestDispatcher("/WEB-INF/views/user/products.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void showDetail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int productId = parseIntParam(req.getParameter("id"), 0);
            Product product = productService.getById(productId);
            if (product == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            req.setAttribute("product", product);

            HttpSession session = req.getSession(false);
            if (SessionUtil.isLoggedIn(session)) {
                int userId = SessionUtil.getUser(session).getUserId();
                req.setAttribute("inWishlist", wishlistService.isInWishlist(userId, productId));
            }
            req.getRequestDispatcher("/WEB-INF/views/user/productDetails.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void showSearch(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String keyword = req.getParameter("q");
            req.setAttribute("products", productService.search(keyword));
            req.setAttribute("keyword",  keyword);
            req.getRequestDispatcher("/WEB-INF/views/user/search.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private int parseIntParam(String value, int defaultValue) {
        try { return Integer.parseInt(value); }
        catch (Exception e) { return defaultValue; }
    }
}
