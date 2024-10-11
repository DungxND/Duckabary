package io.vn.dungxnd.duckabary.core.user_management;

public class UserServices {
    private final UserManagement userManager;

    public UserServices(UserManagement userManager) {
        this.userManager = userManager;
    }

    public void createUser(
            String username,
            String email,
            String firstName,
            String lastName,
            String phone,
            String address) {
        userManager.createUser(username, email, firstName, lastName, phone, address);
    }
}
