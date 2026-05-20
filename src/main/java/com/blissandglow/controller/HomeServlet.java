package com.blissandglow.controller;

import com.blissandglow.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

// Handles the homepage — loads a few products for each section
@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    // Category IDs matching the database
    private static final int[] SKINCARE_IDS = {1, 2, 3, 4, 7};
    private static final int[] MAKEUP_IDS   = {5, 6};
    private static final int[] HAIRCARE_IDS = {8};

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // Load up to 4 products per section for the homepage
            req.setAttribute("skincareProducts", limit(productService.getByCategories(SKINCARE_IDS), 4));
            req.setAttribute("makeupProducts",   limit(productService.getByCategories(MAKEUP_IDS),   4));
            req.setAttribute("haircareProducts", limit(productService.getByCategories(HAIRCARE_IDS), 4));
        } catch (Exception e) {
            // If database fails, show the page with empty sections rather than crashing
            LOG("Could not load homepage products: " + e.getMessage());
        }
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    // Helper to return only the first N items from a list
    private List<?> limit(List<?> list, int max) {
        return list.size() > max ? list.subList(0, max) : list;
    }

    private void LOG(String msg) {
        getServletContext().log(msg);
    }
}