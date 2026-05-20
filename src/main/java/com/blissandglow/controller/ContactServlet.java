package com.blissandglow.controller;

import com.blissandglow.dao.ContactDAO;
import com.blissandglow.model.ContactMessage;
import com.blissandglow.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/contact")
public class ContactServlet extends HttpServlet {

    private final ContactDAO contactDAO = new ContactDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/contact.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name    = ValidationUtil.sanitize(req.getParameter("name"));
        String email   = ValidationUtil.sanitize(req.getParameter("email"));
        String subject = ValidationUtil.sanitize(req.getParameter("subject"));
        String message = ValidationUtil.sanitize(req.getParameter("message"));

        if (ValidationUtil.isBlank(name) || !ValidationUtil.isValidEmail(email)
                || ValidationUtil.isBlank(subject) || ValidationUtil.isBlank(message)) {
            req.setAttribute("error", "Please fill in all fields with valid data.");
            req.getRequestDispatcher("/contact.jsp").forward(req, resp);
            return;
        }

        try {
            ContactMessage msg = new ContactMessage();
            msg.setName(name);
            msg.setEmail(email);
            msg.setSubject(subject);
            msg.setMessage(message);
            contactDAO.insert(msg);
            resp.sendRedirect(req.getContextPath() + "/contact?msg=sent");
        } catch (Exception e) {
            req.setAttribute("error", "Could not send your message. Please try again.");
            req.getRequestDispatcher("/contact.jsp").forward(req, resp);
        }
    }
}
