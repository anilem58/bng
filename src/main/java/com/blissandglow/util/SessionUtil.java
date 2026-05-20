package com.blissandglow.util;

import com.blissandglow.model.User;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    public static final String USER_KEY = "loggedInUser";

    private SessionUtil() {}

    public static void setUser(HttpSession session, User user) {
        session.setAttribute(USER_KEY, user);
    }

    public static User getUser(HttpSession session) {
        return (User) session.getAttribute(USER_KEY);
    }

    public static boolean isLoggedIn(HttpSession session) {
        return getUser(session) != null;
    }

    public static boolean isAdmin(HttpSession session) {
        User u = getUser(session);
        return u != null && "ADMIN".equals(u.getRole());
    }

    public static boolean isCustomer(HttpSession session) {
        User u = getUser(session);
        return u != null && "CUSTOMER".equals(u.getRole());
    }

    public static void invalidate(HttpSession session) {
        if (session != null) session.invalidate();
    }
}
