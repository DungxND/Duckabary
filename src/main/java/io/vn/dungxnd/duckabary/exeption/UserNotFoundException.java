package io.vn.dungxnd.duckabary.exeption;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int userId) {
        super("User not found with ID: " + userId);
    }
}
