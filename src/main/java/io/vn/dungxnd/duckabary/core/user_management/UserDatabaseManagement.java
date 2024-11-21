package io.vn.dungxnd.duckabary.core.user_management;

import io.vn.dungxnd.duckabary.core.exeption.DatabaseException;
import io.vn.dungxnd.duckabary.core.db.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDatabaseManagement {

    private Connection conn;

    public ArrayList<User> loadUsersFromDB() {
        ArrayList<User> users = new ArrayList<>();

        String sql = "SELECT * FROM users";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.isClosed()) {
                System.out.println("No user data found in database!");
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

                User user =
                        User.createUser(
                                userId, username, firstName, lastName, email, phone, address);

                users.add(user);
            }

        } catch (SQLException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }

        if (!users.isEmpty()) {
            System.out.printf("Loaded %d user(s) successfully!\n", users.size());
        } else {
            System.out.println("No user data found in database!");
        }

        return users;
    }

    public void addUserToDB(User user) {
        String sql =
                "INSERT INTO users (username, firstname, lastname, email, phone, address) VALUES ( ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, user.username());
                pstmt.setString(2, user.firstName());
                pstmt.setString(3, user.lastName());
                pstmt.setString(4, user.email());
                pstmt.setString(5, user.phone());
                pstmt.setString(6, user.address());

                pstmt.executeUpdate();
                conn.commit();

                System.out.println("User added to DB successfully!");

            } catch (SQLException e) {
                System.out.println("Error adding user: " + e.getMessage());
                conn.rollback();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to add user to database", e);
        }
    }

    public void addAdminUserToDB(AdminUser user) {
        String sql = "INSERT INTO admins (username, email, hashedPassword) VALUES (?,?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.username());
            pstmt.setString(2, user.email());
            pstmt.setString(3, user.hashedPassword());

            pstmt.executeUpdate();

            System.out.println("Admin user added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding admin user: " + e.getMessage());
        }
    }

    public ArrayList<AdminUser> loadAdminUsersFromDB() {
        ArrayList<AdminUser> admins = new ArrayList<>();

        String sql = "SELECT * FROM admins";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.isClosed()) {
                System.out.println("No admin user data found in database!");
                return admins;
            }
            // Get data from loaded users db columns
            while (rs.next()) {
                int adminId = rs.getInt("admin_id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String hashedPassword = rs.getString("hashedPassword");

                AdminUser user = AdminUser.createAdmin(adminId, username, email, hashedPassword);
                admins.add(user);
            }

        } catch (SQLException e) {
            System.out.println("Error loading admin users: " + e.getMessage());
        }

        if (!admins.isEmpty()) {
            System.out.printf("Loaded %d admin user(s) successfully!\n", admins.size());
        } else {
            System.out.println("No admin user data found in database!");
        }
        return admins;
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

    public void updateUserInDB(User updatedUser) {
        String sql =
                "UPDATE users SET username = ?, firstname = ?, lastname = ?, email = ?, phone = ?, address = ? WHERE user_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, updatedUser.username());
            pstmt.setString(2, updatedUser.firstName());
            pstmt.setString(3, updatedUser.lastName());
            pstmt.setString(4, updatedUser.email());
            pstmt.setString(5, updatedUser.phone());
            pstmt.setString(6, updatedUser.address());
            pstmt.setInt(7, updatedUser.id());

            pstmt.executeUpdate();

            System.out.println("User updated in DB successfully!");

        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }
}
