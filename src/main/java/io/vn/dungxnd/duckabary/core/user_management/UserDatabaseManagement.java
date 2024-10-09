package io.vn.dungxnd.duckabary.core.user_management;

import io.vn.dungxnd.duckabary.db.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDatabaseManagement {
    private static final String DB_PATH = "src/main/resources/io/vn/dungxnd/duckabary/db/duckabary.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;
    private Connection conn;

    public ArrayList<User> loadUsersFromDB() {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String username = rs.getString("username");
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");

                User user = new User(userId, username, firstName, lastName, email, phone, address);
                users.add(user);
            }

        } catch (SQLException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        System.out.println("Users loaded successfully!");
        return users;
    }

    public void addUserToDB(User user) {
        String sql = "INSERT INTO users (user_id, username, firstname, lastname, email, phone, address) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getFirstName());
            pstmt.setString(3, user.getLastName());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, user.getAddress());

            pstmt.executeUpdate();

            System.out.println("User added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }
}
