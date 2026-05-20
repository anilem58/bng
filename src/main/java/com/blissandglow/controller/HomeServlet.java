package com.blissandglow.controller;

import com.blissandglow.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    private static final int[] SKINCARE_IDS  = {1, 2, 3, 4, 7};
    private static final int[] MAKEUP_IDS    = {5, 6};
    private static final int[] HAIRCARE_IDS  = {8};

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("skincareProducts",  limit(productService.getByCategories(SKINCARE_IDS), 4));
            req.setAttribute("makeupProducts",    limit(productService.getByCategories(MAKEUP_IDS),   4));
            req.setAttribute("haircareProducts",  limit(productService.getByCategories(HAIRCARE_IDS), 4));
        } catch (Exception e) {
            // Serve page without products rather than crashing
        }
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    private java.util.List<?> limit(java.util.List<?> list, int max) {
        return list.size() > max ? list.subList(0, max) : list;
    }
}
