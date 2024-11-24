package io.vn.dungxnd.duckabary.util;

import com.password4j.Argon2Function;
import com.password4j.Password;
import com.password4j.types.Argon2;

public class PasswordUtils {

    /* Hash a password for storing. */
    private static final Argon2Function argon2 =
            Argon2Function.getInstance(131072, 5, 4, 64, Argon2.ID, 19);

    private PasswordUtils() {}

    public static String hashPassword(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        return Password.hash(plainTextPassword).addRandomSalt().with(argon2).getResult();
    }

    // Verify a password with retrieved DB stored hash
    public static boolean verifyPassword(String plainTextPassword, String storedHash) {
        return Password.check(plainTextPassword, storedHash).withArgon2();
    }
}
