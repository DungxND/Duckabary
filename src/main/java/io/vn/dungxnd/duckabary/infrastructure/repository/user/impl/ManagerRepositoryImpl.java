package io.vn.dungxnd.duckabary.infrastructure.repository.user.impl;

import io.vn.dungxnd.duckabary.domain.database.DatabaseManager;
import io.vn.dungxnd.duckabary.domain.model.user.Manager;
import io.vn.dungxnd.duckabary.exception.DatabaseException;
import io.vn.dungxnd.duckabary.infrastructure.repository.user.ManagerRepository;
import io.vn.dungxnd.duckabary.util.LoggerUtils;
import io.vn.dungxnd.duckabary.util.PasswordUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManagerRepositoryImpl implements ManagerRepository {
    private static final String SELECT_MANAGER =
            """
            SELECT manager_id, username, email, hashedPassword, avatarPath
            FROM manager
            """;

    @Override
    public List<Manager> getAll() {
        List<Manager> managers = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_MANAGER)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                managers.add(mapToManager(rs));
            }
            LoggerUtils.info("Retrieved " + managers.size() + " managers");
            return managers;
        } catch (SQLException e) {
            LoggerUtils.error("Failed to fetch all managers", e);
            throw new DatabaseException("Failed to fetch managers");
        }
    }

    @Override
    public Optional<Manager> searchById(int id) {
        String sql = SELECT_MANAGER + " WHERE manager_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToManager(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            LoggerUtils.error("Error finding manager with id: " + id, e);
            throw new DatabaseException("Failed to fetch manager by id");
        }
    }

    @Override
    public Optional<Manager> searchByUsername(String username) {
        String sql = SELECT_MANAGER + " WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToManager(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            LoggerUtils.error("Error finding manager by username: " + username, e);
            throw new DatabaseException("Failed to fetch manager by username");
        }
    }

    @Override
    public Optional<Manager> searchByEmail(String email) {
        String sql = SELECT_MANAGER + " WHERE email = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToManager(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            LoggerUtils.error("Error finding manager by email: " + email, e);
            throw new DatabaseException("Failed to fetch manager by email");
        }
    }

    @Override
    public Manager save(Manager manager) {
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Manager savedManager =
                        manager.managerId() == 0
                                ? insertManager(conn, manager)
                                : updateManager(conn, manager);
                conn.commit();
                return savedManager;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            LoggerUtils.error("Error saving manager: " + manager.username(), e);
            throw new DatabaseException("Failed to save manager");
        }
    }

    @Override
    public void delete(int id) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM manager WHERE manager_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new DatabaseException("Admin not found with id: " + id);
                }
                LoggerUtils.info("Deleted manager with id: " + id);
            }
        } catch (SQLException e) {
            LoggerUtils.error("Error deleting manager with id: " + id, e);
            throw new DatabaseException("Failed to delete manager");
        }
    }

    @Override
    public boolean validateCredentials(String username, String password) {
        return searchByUsername(username)
                .map(manager -> PasswordUtils.verifyPassword(password, manager.hashedPassword()))
                .orElse(false);
    }

    private Manager mapToManager(ResultSet rs) throws SQLException {
        return new Manager(
                rs.getInt("manager_id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("hashedPassword"),
                rs.getString("avatarPath"));
    }

    private Manager insertManager(Connection conn, Manager manager) throws SQLException {
        String sql =
                "INSERT INTO manager (username, email, hashedPassword, avatarPath) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, manager.username());
            stmt.setString(2, manager.email());
            stmt.setString(3, manager.hashedPassword());
            stmt.setString(4, manager.avatarPath());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating manager failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    return searchById(conn, id)
                            .orElseThrow(
                                    () -> new SQLException("Manager not found with ID: " + id));
                }
                throw new SQLException("Creating manager failed, no ID obtained.");
            }
        }
    }

    @Override
    public Optional<Manager> searchById(Connection conn, int id) {
        String sql = SELECT_MANAGER + " WHERE manager_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(id));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToManager(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            LoggerUtils.error("Error finding manager by id: " + id, e);
            throw new DatabaseException("Failed to fetch manager by id");
        }
    }

    private Manager updateManager(Connection conn, Manager manager) throws SQLException {
        String sql =
                "UPDATE manager SET username = ?, email = ?, hashedPassword = ?, avatarPath = ?  WHERE manager_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, manager.username());
            stmt.setString(2, manager.email());
            stmt.setString(3, manager.hashedPassword());
            stmt.setString(4, manager.avatarPath());
            stmt.setInt(5, manager.managerId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating manager failed, no rows affected.");
            }

            return searchById(manager.managerId()).orElseThrow();
        }
    }
}
