package io.vn.dungxnd.duckabary.core.user_management;

import java.util.ArrayList;

public class UserService {
    private final UserManagement userManager;

    public UserService(UserManagement userManager) {
        this.userManager = userManager;
    }

    public void createUser(
            String username,
            String email,
            String firstName,
            String lastName,
            String phone,
            String address) {
        int userId = getNewUserID();
        User user = new User(userId, username, firstName, lastName, email, phone, address);
        userManager.createUser(user);
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

}
