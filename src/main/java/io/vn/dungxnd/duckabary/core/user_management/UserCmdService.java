package io.vn.dungxnd.duckabary.core.user_management;

import io.vn.dungxnd.duckabary.utils.LoggerUtils;

public class UserCmdService extends UserService {
    public UserCmdService(UserManagement userManager) {
        super(userManager);
        LoggerUtils.debug("UserCmdService initialized");
    }

    @Override
    public User getUserByID(int userId) {
        User user = super.getUserByID(userId);
        if (user == null) {
            LoggerUtils.info("User not found with ID: " + userId);
            System.out.println("User not found");
            return null;
        }
        return user;
    }

    @Override
    public User createUser(
            String username,
            String email,
            String firstName,
            String lastName,
            String phone,
            String address) {
        var user = super.createUser(username, email, firstName, lastName, phone, address);

        if (user == null) {
            System.out.println("User creation failed");
            return null;
        }

        System.out.println(
                "User " + username + " created successfully with id " + (getNewUserID()));
        return user;
    }

    public void getUserInfo(int userId) {
        User foundUser = super.getUserByID(userId);
        if (foundUser == null) {
            System.out.println("User not found");
            return;
        }
        System.out.println("======= UID " + userId + " =======");
        System.out.println("First Name: " + foundUser.firstName());
        System.out.println("Last Name: " + foundUser.lastName());
        System.out.println("Username: " + foundUser.username());
        System.out.println("Email: " + foundUser.email());
        System.out.println("Phone: " + foundUser.phone());
        System.out.println("Address: " + foundUser.address());
        System.out.println("======================");
    }
}
