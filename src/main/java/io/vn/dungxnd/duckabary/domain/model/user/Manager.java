package io.vn.dungxnd.duckabary.domain.model.user;

public record Manager(int managerId, String username, String email, String hashedPassword) {
    public static Manager createManager(
            int id, String username, String email, String hashedPassword) {
        return new Manager(id, username, email, hashedPassword);
    }
}
