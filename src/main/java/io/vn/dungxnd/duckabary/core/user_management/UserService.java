package io.vn.dungxnd.duckabary.core.user_management;

import io.vn.dungxnd.duckabary.utils.LoggerUtils;

import java.util.ArrayList;

public class UserService {
    private final UserManagement userManager;

    public UserService(UserManagement userManager) {
        this.userManager = userManager;
        LoggerUtils.debug("UserService is created");
    }

    public User createUser(
            String username,
            String email,
            String firstName,
            String lastName,
            String phone,
            String address) {
        try {
            validateUserInput(username, email, phone, address);
        } catch (IllegalArgumentException e) {
            LoggerUtils.error("Failed to create user: " + username + ". " + e.getMessage(), e);
            return null;
        }
        int userId = userManager.getNewUserID();
        User user = User.createUser(userId, username, firstName, lastName, email, phone, address);
        userManager.createUser(user);
        LoggerUtils.info("Created new user: " + username);
        return user;

    }

    protected void validateUserInput(String username, String email, String phone, String address) {
        LoggerUtils.debug("Validating user input for username: " + username);

        if (username == null || username.isBlank()) {
            LoggerUtils.warn("Attempted to create user with empty username");
            throw new IllegalArgumentException("Username cannot be empty");
        }

        if (isUsernameAlreadyExist(username)) {
            LoggerUtils.warn("Attempted to create user with existing username: " + username);
            throw new IllegalArgumentException("Username already exists");
        }

        if (username.length() < 5 || username.length() > 16) {
            LoggerUtils.warn("Invalid username length: " + username);
            throw new IllegalArgumentException("Username must be between 5 and 16 characters");
        }

        if (!username.matches("^[A-Za-z0-9_]+$")) {
            LoggerUtils.warn("Invalid username format: " + username);
            throw new IllegalArgumentException("Username must contain only letters, numbers, and underscores");
        }

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            LoggerUtils.warn("Invalid email format: " + email);
            throw new IllegalArgumentException("Invalid email format");
        }

        if (phone == null || !phone.matches("^[0-9]{8,11}$")) {
            LoggerUtils.warn("Invalid phone number: " + phone);
            throw new IllegalArgumentException("Invalid phone number");
        }

        if (address == null || address.isBlank()) {
            LoggerUtils.warn("Empty address provided");
            throw new IllegalArgumentException("Address cannot be empty");
        }

        LoggerUtils.debug("User input validation successful");
    }

    public int getNewUserID() {
        return userManager.getNewUserID();
    }

    public User getUserByID(int userId) {
        if (userId <= 0 || userId > userManager.getUsers().size()) {
            LoggerUtils.warn("Attempted to get user with invalid ID: " + userId);
            return null;
        }
        LoggerUtils.debug("Retrieving user with ID: " + userId);
        return userManager.getUserById(userId);
    }

    public boolean isUsernameAlreadyExist(String username) {
        return userManager.checkUsernameExist(username);
    }

    public ArrayList<User> getUsers() {
        return userManager.getUsers();
    }

    public void updateUser(User user) {
        userManager.updateUser(user);
    }

    public void createAdminUser(int id, String username, String email, String rawPassword) {
        userManager.createAdminUser(id, username, email, rawPassword);
    }
}
