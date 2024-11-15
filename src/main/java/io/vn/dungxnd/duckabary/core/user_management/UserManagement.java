package io.vn.dungxnd.duckabary.core.user_management;

import java.util.ArrayList;

public class UserManagement {
    private final ArrayList<User> users;
    private final ArrayList<AdminUser> adminUsers;
    private final UserDatabaseManagement userDatabaseManagement;

    /**
     * Constructor for UserManagement
     * Initialize the list of users and admin users
     * Load users and admin users from the database
     * Add the loaded users and admin users to the list
     */
    public UserManagement() {
        this.adminUsers = new ArrayList<>();
        this.users = new ArrayList<>();
        this.userDatabaseManagement = new UserDatabaseManagement();
        users.addAll(userDatabaseManagement.loadUsersFromDB());
        adminUsers.addAll(userDatabaseManagement.loadAdminUsersFromDB());
    }

    public User getUserById(int userId) {
        return users.get(userId - 1);
    }

    public void createUser(User user) {
        users.add(user);
        userDatabaseManagement.addUserToDB(user);
    }

    public int getNewUserID() {
        return users.size();
    }

    public int getNewAdminUserID() {
        return adminUsers.size();
    }

    public void createAdminUser(int id, String username, String email, String rawPassword) {
        AdminUser adminUser = new AdminUser(getNewAdminUserID(), username, email, rawPassword);
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

    public boolean checkUsernameExist(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public UserDatabaseManagement getUserDatabaseManagement() {
        return userDatabaseManagement;
    }
}
