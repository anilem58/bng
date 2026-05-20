package com.blissandglow.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Work factor 12 is strong but not too slow (good balance)
    private static final int COST = 12;

    // No instances needed
    private PasswordUtil() {}

    // Hash a plain text password before storing it in the database
    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(COST));
    }

    // Check if a typed password matches the stored hash
    public static boolean verify(String plainPassword, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2")) return false;
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}