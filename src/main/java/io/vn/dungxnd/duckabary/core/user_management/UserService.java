package io.vn.dungxnd.duckabary.core.user_management;

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
        return userManager.getUser(userId);
    }

}
