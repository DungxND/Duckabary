package io.vn.dungxnd.duckabary.core.user_management;

public class User {
    private String username;
    private String email;
    private String hashedPassword;

    public User(String username, String email, String rawPassword) {
        this.username = username;
        this.email = email;
        this.hashedPassword = PasswordUtils.hashPassword(rawPassword);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void changePassword(String rawPassword) {
        this.hashedPassword = PasswordUtils.hashPassword(rawPassword);
    }

}
