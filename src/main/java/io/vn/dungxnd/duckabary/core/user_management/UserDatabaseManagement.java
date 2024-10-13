package io.vn.dungxnd.duckabary.core.user_management;

import io.vn.dungxnd.duckabary.db.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDatabaseManagement {
    private static final String DB_PATH =
            "src/main/resources/io/vn/dungxnd/duckabary/db/duckabary.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;
    private Connection conn;

    static {
        DatabaseManager.checkAndInitDB();
    }

    public ArrayList<User> loadUsersFromDB() {
        ArrayList<User> users = new ArrayList<>();

        String sql = "SELECT * FROM users";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.isClosed()) {
                System.out.println("No user data found!");
                return users;
            }
            // Get data from loaded users db columns
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

        if (!users.isEmpty()) {
            System.out.printf("Loaded %d user(s) successfully!\n", users.size());
        }
        return users;
    }

    public void addUserToDB(User user) {
        String sql =
                "INSERT INTO users (username, firstname, lastname, email, phone, address) VALUES ( ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getFirstName());
            pstmt.setString(3, user.getLastName());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, user.getAddress());

            pstmt.executeUpdate();

            System.out.println("User added to DB successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    public void addAdminUserToDB(AdminUser user) {
        String sql =
                "INSERT INTO admins (username, email, hashedPassword) VALUES (?,?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getHashedPassword());

            pstmt.executeUpdate();

            System.out.println("Admin user added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding admin user: " + e.getMessage());
        }
    }

    public ArrayList<AdminUser> loadAdminUsersFromDB() {
        ArrayList<AdminUser> users = new ArrayList<>();

        String sql = "SELECT * FROM admins";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.isClosed()) {
                System.out.println("No admin user data found!");
                return users;
            }
            // Get data from loaded users db columns
            while (rs.next()) {
                int adminId = rs.getInt("admin_id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String hashedPassword = rs.getString("hashedPassword");

                AdminUser user = new AdminUser(adminId, username, email, hashedPassword);
                users.add(user);
            }

        } catch (SQLException e) {
            System.out.println("Error loading admin users: " + e.getMessage());
        }

        if (!users.isEmpty()) {
            System.out.printf("Loaded %d admin user(s) successfully!\n", users.size());
        }
        return users;
    }

    public void deleteUserFromDB(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.executeUpdate();

            System.out.println("User deleted successfully!");

        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }
}
