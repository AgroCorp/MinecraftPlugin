package me.sativus.testplugin.utils;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptUtil {
    // Hash the password
    public static String hashPassword(String plainPassword) {
        // gensalt() generates a random salt internally
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Verify a plaintext password against the hashed one
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
