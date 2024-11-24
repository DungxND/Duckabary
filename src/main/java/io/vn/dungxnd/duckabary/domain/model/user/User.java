package io.vn.dungxnd.duckabary.domain.model.user;

public record User(
        int id,
        String username,
        String firstName,
        String lastName,
        String email,
        String phone,
        String address) {

    public static User createUser(
            int id,
            String username,
            String firstName,
            String lastName,
            String email,
            String phone,
            String address) {
        return new User(id, username, firstName, lastName, email, phone, address);
    }
}
