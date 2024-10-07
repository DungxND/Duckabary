package io.vn.dungxnd.duckabary.core.user_management;

import com.password4j.Argon2Function;
import com.password4j.Password;
import com.password4j.types.Argon2;

public class PasswordUtils {
    
   /* Hash a password for storing. */
   static Argon2Function argon2 = Argon2Function.getInstance(131072, 5, 4, 40, Argon2.ID, 19);
    public static String hashPassword(String plainTextPassword) {
        return Password.hash(plainTextPassword)
                .with(argon2)
                .getResult();
    }
    

    // Verify a password with retrieved DB stored hash
    public static boolean verifyPassword(String plainTextPassword, String storedHash) {
        return Password.check(plainTextPassword, storedHash).withArgon2();
    }
    

}