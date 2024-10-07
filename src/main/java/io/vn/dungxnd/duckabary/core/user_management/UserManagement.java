package io.vn.dungxnd.duckabary.core.user_management;

public class UserManagement {
    public void createUser(String username, String email, String rawPassword) {
        User user = new User(username, email, rawPassword);
        // TODO: Save user to database

    }
}
