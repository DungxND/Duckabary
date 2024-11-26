package io.vn.dungxnd.duckabary.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int userId) {
        super("User not found with ID: " + userId);
    }
}
