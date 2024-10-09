package io.vn.dungxnd.duckabary.core.user_management;

import java.util.ArrayList;

public class UserManagement {
    private final ArrayList<User> users;
    private final UserDatabaseManagement userDatabaseManagement = new UserDatabaseManagement();

    public UserManagement() {
        this.users = new ArrayList<>();
        users.addAll(userDatabaseManagement.loadUsersFromDB());
    }

    public void getUserInfo(int userId) {
        if (userId < 0 || userId >= users.size()) {
            System.out.println("User not found");
            return;
        }
        System.out.println("======= UID " + users.get(userId).getId() + " =======");
        System.out.println("First Name: " + users.get(userId).getFirstName());
        System.out.println("Last Name: " + users.get(userId).getLastName());
        System.out.println("Username: " + users.get(userId).getUsername());
        System.out.println("Email: " + users.get(userId).getEmail());
        System.out.println("Phone: " + users.get(userId).getPhone());
        System.out.println("Address: " + users.get(userId).getAddress());
        System.out.println("======================");
    }

    public void createUser(
            String username,
            String email,
            String firstName,
            String lastName,
            String phone,
            String address) {
        User user =
                new User(users.size() + 1, username, firstName, lastName, email, phone, address);
        users.add(user);
        userDatabaseManagement.addUserToDB(user);
    }

    public void createAdminUser(String username, String email, String rawPassword) {
        AdminUser adminUser = new AdminUser(username, email, rawPassword);
        users.add(adminUser);
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
