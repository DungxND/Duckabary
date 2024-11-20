package io.vn.dungxnd.duckabary.core.user_management;

import java.util.ArrayList;

public class UserService {
    private final UserManagement userManager;

    public UserService(UserManagement userManager) {
        this.userManager = userManager;
    }

    public User createUser(
            String username,
            String email,
            String firstName,
            String lastName,
            String phone,
            String address) {
        validateUserInput(username, email, phone, address);
        int userId = userManager.getNewUserID();
        User user = User.createUser(userId, username, firstName, lastName, email, phone, address);
        userManager.createUser(user);
        return user;
    }

    protected void validateUserInput(String username, String email, String phone, String address) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        if (isUsernameAlreadyExist(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (username.length() < 5 || username.length() > 20) {
            throw new IllegalArgumentException("Username must be between 5 and 20 characters");
        }

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (phone == null || !phone.matches("^[0-9]{8,11}$")) {
            throw new IllegalArgumentException("Invalid phone number");
        }

        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }
    }

    public int getNewUserID() {
        return userManager.getNewUserID();
    }

    public User getUser(int userId) {
        if (userId <= 0 || userId > userManager.getUsers().size()) {
            return null;
        }
        return userManager.getUserById(userId);
    }

    public boolean isUsernameAlreadyExist(String username) {
        return userManager.checkUsernameExist(username);
    }

    public User getUserByID(int userId) {
        return userManager.getUserById(userId);
    }

    public ArrayList<User> getUsers() {
        return userManager.getUsers();
    }

    public void updateUser(User user) {
        userManager.updateUser(user);
    }
}
