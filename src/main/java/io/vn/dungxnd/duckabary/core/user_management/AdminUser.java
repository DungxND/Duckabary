package io.vn.dungxnd.duckabary.core.user_management;

public record AdminUser(int adminId, String username, String email, String hashedPassword) {
    public static AdminUser createAdmin(int id, String username, String email, String rawPassword) {
        return new AdminUser(id, username, email, PasswordUtils.hashPassword(rawPassword));
    }

    public AdminUser withUsername(String newUsername) {
        return new AdminUser(adminId, newUsername, email, hashedPassword);
    }

    public AdminUser withEmail(String newEmail) {
        return new AdminUser(adminId, username, newEmail, hashedPassword);
    }

    public AdminUser withNewPassword(String rawPassword) {
        return new AdminUser(adminId, username, email, PasswordUtils.hashPassword(rawPassword));
    }
}
