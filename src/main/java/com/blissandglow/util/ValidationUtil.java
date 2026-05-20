package com.blissandglow.util;

import java.util.regex.Pattern;

public class ValidationUtil {

    // Email pattern: standard format like user@example.com
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[\\w.+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

    // Phone pattern: 7 to 15 digits only
    private static final Pattern PHONE_PATTERN =
        Pattern.compile("^[0-9]{7,15}$");

    // Password must have: 8+ chars, 1 uppercase, 1 lowercase, 1 digit, 1 special character
    private static final Pattern PASSWORD_PATTERN =
        Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$!%*?&]).{8,}$");

    private ValidationUtil() {}

    // Returns true if value is null or just whitespace
    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    // Returns true if email looks valid
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    // Returns true if phone number is 7-15 digits
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    // Returns true if password meets strength requirements
    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    // Returns true if the number is greater than zero
    public static boolean isPositive(double value) {
        return value > 0;
    }

    // Returns true if the number is zero or more
    public static boolean isNonNegative(int value) {
        return value >= 0;
    }

    // Trim whitespace from a value; return empty string if null
    public static String sanitize(String value) {
        return value == null ? "" : value.trim();
    }
}