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

    public static boolean isValidISBN(String isbn) {
        isbn = isbn.replaceAll("[-\\s]", "");

        if (isbn.length() == 10) {
            return isValidIsbn10(isbn);
        }

        if (isbn.length() == 13) {
            return isValidIsbn13(isbn);
        }

        return false;
    }

    private static boolean isValidIsbn10(String isbn) {
        int sum = 0;

        for (int i = 0; i < 9; i++) {
            char c = isbn.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
            sum += (c - '0') * (10 - i);
        }

        char last = isbn.charAt(9);
        if (last == 'X') {
            sum += 10;
        } else if (Character.isDigit(last)) {
            sum += (last - '0');
        } else {
            return false;
        }

        return sum % 11 == 0;
    }

    private static boolean isValidIsbn13(String isbn) {
        int sum = 0;

        for (int i = 0; i < 12; i++) {
            char c = isbn.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
            sum += (c - '0') * (i % 2 == 0 ? 1 : 3);
        }

        char last = isbn.charAt(12);
        if (!Character.isDigit(last)) {
            return false;
        }

        int checksum = (10 - (sum % 10)) % 10;
        return checksum == (last - '0');
    }

    public static boolean isValidISSN(String issn) {
        issn = issn.replaceAll("[-\\s]", "");

        if (issn.length() != 8) {
            return false;
        }

        for (int i = 0; i < 7; i++) {
            if (!Character.isDigit(issn.charAt(i))) {
                return false;
            }
        }

        char lastChar = issn.charAt(7);
        if (lastChar != 'X' && !Character.isDigit(lastChar)) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 7; i++) {
            sum += (8 - i) * (issn.charAt(i) - '0');
        }

        if (lastChar == 'X') {
            sum += 10;
        } else {
            sum += (lastChar - '0');
        }

        return sum % 11 == 0;
    }
}
