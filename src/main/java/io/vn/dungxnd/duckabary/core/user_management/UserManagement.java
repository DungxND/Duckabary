package io.vn.dungxnd.duckabary.core.user_management;

import java.util.ArrayList;

public class UserManagement {
    private final ArrayList<User> users;
    private final ArrayList<AdminUser> adminUsers;
    private final UserDatabaseManagement userDatabaseManagement;

    private final Object lock = new Object();

    /**
     * Constructor for UserManagement Initialize the list of users and admin users Load users and
     * admin users from the database Add the loaded users and admin users to the list
     */
    public UserManagement() {
        this.adminUsers = new ArrayList<>();
        this.users = new ArrayList<>();
        this.userDatabaseManagement = new UserDatabaseManagement();
        users.addAll(userDatabaseManagement.loadUsersFromDB());
        adminUsers.addAll(userDatabaseManagement.loadAdminUsersFromDB());
    }

    public User getUserById(int userId) {
        return users.stream()
                .filter(user -> user.id() == userId - 1)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public void createUser(User user) {
        synchronized (lock) {
            if (checkUsernameExist(user.username())) {
                throw new IllegalArgumentException("Username already exists");
            }
            users.add(user);
            userDatabaseManagement.addUserToDB(user);
        }
    }

    public int getNewUserID() {
        return users.size();
    }

    public int getNewAdminUserID() {
        return adminUsers.size();
    }

    public void createAdminUser(int id, String username, String email, String rawPassword) {
        AdminUser adminUser =
                AdminUser.createAdmin(getNewAdminUserID(), username, email, rawPassword);
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
            if (user.username().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public UserDatabaseManagement getUserDatabaseManagement() {
        return userDatabaseManagement;
    }

    public boolean updateUser(User updatedUser) {
        int index = findUserIndexById(updatedUser.id());

        if (index != -1) {
            users.set(index, updatedUser);

            userDatabaseManagement.updateUserInDB(updatedUser);
            return true;
        } else {
            return false;
        }
    }

    private int findUserIndexById(int userId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).id() == userId) {
                return i;
            }
        }
        return -1;
    }
}
