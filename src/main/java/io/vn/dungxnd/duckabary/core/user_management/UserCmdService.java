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
    public void createUser(
            String username,
            String email,
            String firstName,
            String lastName,
            String phone,
            String address) {
        if (super.isUsernameAlreadyExist(username)) {
            System.out.println("Username " + username + " already exists, please use another one");
            return;
        }
        super.createUser(username, email, firstName, lastName, phone, address);
        System.out.println(
                "User " + username + " created successfully with id " + (getNewUserID()));
    }

    public void getUserInfo(int userId) {
        User foundUser = super.getUser(userId);
        if (foundUser == null) {
            System.out.println("User not found");
            return;
        }
        System.out.println("======= UID " + userId + " =======");
        System.out.println("First Name: " + foundUser.getFirstName());
        System.out.println("Last Name: " + foundUser.getLastName());
        System.out.println("Username: " + foundUser.getUsername());
        System.out.println("Email: " + foundUser.getEmail());
        System.out.println("Phone: " + foundUser.getPhone());
        System.out.println("Address: " + foundUser.getAddress());
        System.out.println("======================");
    }

}
