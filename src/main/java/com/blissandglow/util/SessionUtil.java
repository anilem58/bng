package com.blissandglow.util;

import com.blissandglow.model.User;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    // The key used to store the logged-in user in the session
    public static final String USER_KEY = "loggedInUser";

    private SessionUtil() {}

    // Save the logged-in user to the session
    public static void setUser(HttpSession session, User user) {
        session.setAttribute(USER_KEY, user);
    }

    // Get the logged-in user from the session
    public static User getUser(HttpSession session) {
        return (User) session.getAttribute(USER_KEY);
    }

    // Check if anyone is currently logged in
    public static boolean isLoggedIn(HttpSession session) {
        return getUser(session) != null;
    }

    // Check if the logged-in user is an admin
    public static boolean isAdmin(HttpSession session) {
        User u = getUser(session);
        return u != null && "ADMIN".equals(u.getRole());
    }

    // Check if the logged-in user is a customer
    public static boolean isCustomer(HttpSession session) {
        User u = getUser(session);
        return u != null && "CUSTOMER".equals(u.getRole());
    }

    // Log out by destroying the session
    public static void invalidate(HttpSession session) {
        if (session != null) session.invalidate();
    }
}