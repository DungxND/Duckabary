package io.vn.dungxnd.duckabary.core.user_management;

public class AdminUser extends User {
    private String hashedPassword;

    public AdminUser(String username, String email, String rawPassword) {
        super(username, email);
        this.hashedPassword = PasswordUtils.hashPassword(rawPassword);
    }

    public void changePassword(String rawPassword) {
        this.hashedPassword = PasswordUtils.hashPassword(rawPassword);
    }
}
