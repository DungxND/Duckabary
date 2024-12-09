package io.vn.dungxnd.duckabary.domain.model.user;

public record Manager(
        int managerId, String username, String email, String hashedPassword, String avatarPath) {
    public static Manager createManager(
            int id, String username, String email, String hashedPassword) {
        return new Manager(id, username, email, hashedPassword, null);
    }

    public static Manager createManager(
            int id, String username, String email, String hashedPassword, String avatarPath) {
        return new Manager(id, username, email, hashedPassword, avatarPath);
    }

    public Manager withAvatarPath(String avatarPath) {
        return new Manager(managerId, username, email, hashedPassword, avatarPath);
    }
}
