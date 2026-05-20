package com.blissandglow.controller;

import com.blissandglow.model.Product;
import com.blissandglow.service.ProductService;
import com.blissandglow.service.WishlistService;
import com.blissandglow.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

// Handles the shop page, product detail page, and search
@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private final ProductService  productService  = new ProductService();
    private final WishlistService wishlistService = new WishlistService();

    // Category IDs from the database
    private static final int[] SKINCARE_IDS = {1, 2, 3, 4, 7};
    private static final int[] MAKEUP_IDS   = {5, 6};
    private static final int[] HAIRCARE_IDS = {8};

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("detail".equals(action)) {
            showProductDetail(req, resp);
        } else if ("search".equals(action)) {
            showSearchResults(req, resp);
        } else {
            showProductList(req, resp);
        }
    }

    // Show all products grouped by category
    private void showProductList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("skincareProducts", productService.getByCategories(SKINCARE_IDS));
            req.setAttribute("makeupProducts",   productService.getByCategories(MAKEUP_IDS));
            req.setAttribute("haircareProducts", productService.getByCategories(HAIRCARE_IDS));
            forward(req, resp, "/WEB-INF/views/user/products.jsp");
        } catch (Exception e) { throw new ServletException(e); }
    }

    // Show a single product's full detail page
    private void showProductDetail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int productId = parseId(req.getParameter("id"));
            Product product = productService.getById(productId);

            if (product == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            req.setAttribute("product", product);

            // Check if this product is in the logged-in customer's wishlist
            HttpSession session = req.getSession(false);
            if (SessionUtil.isLoggedIn(session) && SessionUtil.isCustomer(session)) {
                int userId = SessionUtil.getUser(session).getUserId();
                req.setAttribute("inWishlist", wishlistService.isInWishlist(userId, productId));
            }
            forward(req, resp, "/WEB-INF/views/user/productDetails.jsp");
        } catch (Exception e) { throw new ServletException(e); }
    }

    // Show search results for a keyword
    private void showSearchResults(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String keyword = req.getParameter("q");
            req.setAttribute("products", productService.search(keyword));
            req.setAttribute("keyword",  keyword);
            forward(req, resp, "/WEB-INF/views/user/search.jsp");
        } catch (Exception e) { throw new ServletException(e); }
    }

    private int parseId(String value) {
        try { return Integer.parseInt(value); }
        catch (Exception e) { return 0; }
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp, String path)
            throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, resp);
    }
}