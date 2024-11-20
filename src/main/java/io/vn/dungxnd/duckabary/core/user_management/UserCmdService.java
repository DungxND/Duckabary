package io.vn.dungxnd.duckabary.core.user_management;

public class UserCmdService extends UserService {
    public UserCmdService(UserManagement userManager) {
        super(userManager);
    }

    @Override
    public User getUser(int userId) {
        if (super.getUser(userId) == null) {
            System.out.println("User not found");
            return null;
        }
        return super.getUser(userId);
    }

    @Override
    public User createUser(
            String username,
            String email,
            String firstName,
            String lastName,
            String phone,
            String address) {
        super.createUser(username, email, firstName, lastName, phone, address);
        System.out.println(
                "User " + username + " created successfully with id " + (getNewUserID()));
        return getUser(getNewUserID());
    }

    public void getUserInfo(int userId) {
        User foundUser = super.getUser(userId);
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
