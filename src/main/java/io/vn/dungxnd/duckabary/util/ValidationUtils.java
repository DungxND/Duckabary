package io.vn.dungxnd.duckabary.util;

public class ValidationUtils {
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_-]{5,20}$";

    public static void validateRequiredEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public static void validateEmail(String email) {
        if (!email.trim().isEmpty() && !email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Invalid email format (Email not required)");
        }
    }

    public static void validateRequiredUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (!username.matches(USERNAME_REGEX)) {
            throw new IllegalArgumentException(
                    "Username must be 5-20 characters long and contain only letters, numbers, underscore or hyphen");
        }
    }

    public static void validateUsername(String username) {
        if (username != null && !username.matches(USERNAME_REGEX)) {
            throw new IllegalArgumentException(
                    "Username must be 5-20 characters long and contain only letters, numbers, underscore or hyphen");
        }
    }

    public static void validateRawPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
    }

    public static void validateHashedPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
    }

    public static void validatePasswordMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
    }

    public static void validateRequiredPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }
        if (!phone.matches("^[0-9]{8,11}$")) {
            throw new IllegalArgumentException("Invalid phone number");
        }
    }

    public static void validatePhone(String phone) {
        if (!phone.trim().isEmpty() && !phone.matches("^[0-9]{8,11}$")) {
            throw new IllegalArgumentException("Invalid phone number (Phone not required)");
        }
    }
}
