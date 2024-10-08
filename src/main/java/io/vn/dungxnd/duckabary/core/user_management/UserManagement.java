package io.vn.dungxnd.duckabary.core.user_management;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class UserManagement {
    private ArrayList<User> users;

    public void getUserInfo(int userId) {
        if (userId < 0 || userId >= users.size()) {
            System.out.println("User not found");
            return;
        }
        System.out.println("Username: " + users.get(userId).getUsername());
        System.out.println("Email: " + users.get(userId).getEmail());
    }

    public void createUser(String username, String email) {
        User user = new User(username, email);
        users.add(user);
        // TODO: Save user to database

    }

    public void addUser(User user) {
        users.add(user);
    }

    public void createAdminUser(String username, String email, String rawPassword) {
        AdminUser adminUser = new AdminUser(username, email, rawPassword);
        users.add(adminUser);
    }

    public ArrayList<User> getUsers() {
        return users;
    }


}
