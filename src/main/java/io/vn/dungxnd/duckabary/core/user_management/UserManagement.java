package io.vn.dungxnd.duckabary.core.user_management;

import java.util.ArrayList;

public class UserManagement {
    private final ArrayList<User> users;
    private final ArrayList<AdminUser> adminUsers;
    private final UserDatabaseManagement userDatabaseManagement = new UserDatabaseManagement();

    public UserManagement() {
        this.adminUsers = new ArrayList<>();
        this.users = new ArrayList<>();
        users.addAll(userDatabaseManagement.loadUsersFromDB());
        adminUsers.addAll(userDatabaseManagement.loadAdminUsersFromDB());
    }

    public User getUser(int userId) {
        return users.get(userId-1);
    }

    public void createUser(User user) {
        users.add(user);
        userDatabaseManagement.addUserToDB(user);
    }

    public int getNewUserID() {
        return users.size();
    }

    public void createAdminUser(int id, String username, String email, String rawPassword) {
        AdminUser adminUser = new AdminUser(adminUsers.size()+1, username, email, rawPassword);
        adminUsers.add(adminUser);
    }

    public void deleteUser(int userId) {
        if (userId < 0 || userId >= users.size()) {
            System.out.println("User not found");
            return;
        }
        users.remove(userId);
        userDatabaseManagement.deleteUserFromDB(userId);
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
