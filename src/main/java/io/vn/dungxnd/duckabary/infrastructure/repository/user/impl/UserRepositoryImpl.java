package io.vn.dungxnd.duckabary.infrastructure.repository.user.impl;

import io.vn.dungxnd.duckabary.domain.database.DatabaseManager;
import io.vn.dungxnd.duckabary.domain.model.user.User;
import io.vn.dungxnd.duckabary.exception.DatabaseException;
import io.vn.dungxnd.duckabary.infrastructure.repository.user.UserRepository;
import io.vn.dungxnd.duckabary.util.LoggerUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private static final String SELECT_USER =
            """
            SELECT user_id, username, firstname, lastname,
                   email, phone, address, registration_date
            FROM user
            """;

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_USER)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(mapToUser(rs));
            }
            LoggerUtils.info("Retrieved " + users.size() + " users");
            return users;
        } catch (SQLException e) {
            LoggerUtils.error("Failed to fetch all users", e);
            throw new DatabaseException("Failed to fetch users");
        }
    }

    @Override
    public Optional<User> searchById(int id) {
        String sql = SELECT_USER + " WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToUser(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            LoggerUtils.error("Error finding user with id: " + id, e);
            throw new DatabaseException("Failed to fetch user by id");
        }
    }

    @Override
    public Optional<User> searchById(Connection conn, int id) {
        String sql = SELECT_USER + " WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToUser(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            LoggerUtils.error("Error finding user with id: " + id, e);
            throw new DatabaseException("Failed to fetch user by id");
        }
    }

    @Override
    public Optional<User> searchByUsername(String username) {
        String sql = SELECT_USER + " WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToUser(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            LoggerUtils.error("Error finding user by username: " + username, e);
            throw new DatabaseException("Failed to fetch user by username");
        }
    }

    @Override
    public Optional<User> searchByEmail(String email) {
        String sql = SELECT_USER + " WHERE email = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToUser(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            LoggerUtils.error("Error finding user by email: " + email, e);
            throw new DatabaseException("Failed to fetch user by email");
        }
    }

    @Override
    public User save(User user) {
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                User savedUser = user.id() == 0 ? insertUser(conn, user) : updateUser(conn, user);
                conn.commit();
                return savedUser;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            LoggerUtils.error("Error saving user: " + user.username(), e);
            throw new DatabaseException("Failed to save user");
        }
    }

    @Override
    public void delete(int id) {
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                String sql = "DELETE FROM user WHERE user_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, id);
                    int affectedRows = stmt.executeUpdate();
                    if (affectedRows == 0) {
                        throw new DatabaseException("User not found with id: " + id);
                    }
                }
                conn.commit();
                LoggerUtils.info("Deleted user with id: " + id);
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            LoggerUtils.error("Error deleting user with id: " + id, e);
            throw new DatabaseException("Failed to delete user");
        }
    }

    @Override
    public List<User> searchByName(String name) {
        String sql = SELECT_USER + " WHERE firstname LIKE ? OR lastname LIKE ?";
        List<User> users = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + name + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(mapToUser(rs));
            }
            LoggerUtils.info("Found " + users.size() + " users matching name: " + name);
            return users;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding users by name: " + name, e);
            throw new DatabaseException("Failed to find users by name");
        }
    }

    private User insertUser(Connection conn, User user) throws SQLException {
        String sql =
                """
        INSERT INTO user (username, firstname, lastname, email, phone, address)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.username());
            stmt.setString(2, user.firstName());
            stmt.setString(3, user.lastName());
            stmt.setString(4, user.email());
            stmt.setString(5, user.phone());
            stmt.setString(6, user.address());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                return searchById(conn, id)
                        .orElseThrow(() -> new SQLException("User not found with ID: " + id));
            }
            throw new SQLException("Creating user failed, no ID obtained.");
        }
    }

    private User updateUser(Connection conn, User user) throws SQLException {
        String sql =
                """
        UPDATE user
        SET username = ?, firstname = ?, lastname = ?,
            email = ?, phone = ?, address = ?
        WHERE user_id = ?
        """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.username());
            stmt.setString(2, user.firstName());
            stmt.setString(3, user.lastName());
            stmt.setString(4, user.email());
            stmt.setString(5, user.phone());
            stmt.setString(6, user.address());
            stmt.setInt(7, user.id());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }

            return searchById(conn, user.id()).orElseThrow();
        }
    }

    private User mapToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("username"),
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("address"));
    }
}
