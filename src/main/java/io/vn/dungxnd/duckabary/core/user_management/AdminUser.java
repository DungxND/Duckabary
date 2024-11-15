package io.vn.dungxnd.duckabary.core.user_management;

public class AdminUser {
    private final int adminId;
    private String username;
    private String email;
    private String hashedPassword;

    public AdminUser(int id, String username, String email, String rawPassword) {
        this.adminId = id;
        this.username = username;
        this.email = email;
        this.hashedPassword = PasswordUtils.hashPassword(rawPassword);
    }

    public int getAdminId() {
        return adminId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void changeUsername(String newUsername) {
        this.username = newUsername;
    }

    public void changeEmail(String newEmail) {
        this.email = newEmail;
    }

    public void changePassword(String rawPassword) {
        this.hashedPassword = PasswordUtils.hashPassword(rawPassword);
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
}
