package io.vn.dungxnd.duckabary.core.exeption;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int userId) {
        super("User not found with ID: " + userId);
    }
}
