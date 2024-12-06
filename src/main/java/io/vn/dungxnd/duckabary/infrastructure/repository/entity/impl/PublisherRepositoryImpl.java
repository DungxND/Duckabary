package io.vn.dungxnd.duckabary.infrastructure.repository.entity.impl;

import io.vn.dungxnd.duckabary.domain.database.DatabaseManager;
import io.vn.dungxnd.duckabary.domain.model.entity.Publisher;
import io.vn.dungxnd.duckabary.exception.DatabaseException;
import io.vn.dungxnd.duckabary.infrastructure.repository.entity.PublisherRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PublisherRepositoryImpl implements PublisherRepository {
    @Override
    public List<Publisher> getAll() {
        String sql = "SELECT * FROM publisher";
        List<Publisher> publishers = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                publishers.add(mapToPublisher(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving publishers", e);
        }
        return publishers;
    }

    @Override
    public Optional<Publisher> searchById(Long id) {
        String sql = "SELECT * FROM publisher WHERE publisher_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapToPublisher(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding publisher_id with id: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Publisher> searchByName(String name) {
        String sql = "SELECT * FROM publisher WHERE name = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapToPublisher(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding publisher_id with name: " + name, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Publisher> searchByNamePattern(String namePattern) {
        String sql = "SELECT * FROM publisher WHERE name LIKE ?";
        List<Publisher> publishers = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + namePattern + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                publishers.add(mapToPublisher(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding publishers with pattern: " + namePattern, e);
        }
        return publishers;
    }

    @Override
    public Publisher save(Publisher publisher) {
        if (publisher.id() == null) {
            return insertPublisher(publisher);
        }
        return updatePublisher(publisher);
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM publisher WHERE publisher_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting publisher_id with id: " + id, e);
        }
    }

    private Publisher mapToPublisher(ResultSet rs) throws SQLException {
        return new Publisher(
                rs.getLong("publisher_id"),
                rs.getString("name"),
                Optional.ofNullable(rs.getString("email")),
                Optional.ofNullable(rs.getString("phone")),
                Optional.ofNullable(rs.getString("address")));
    }

    private Publisher insertPublisher(Publisher publisher) {
        String sql = "INSERT INTO publisher (name, email, phone, address) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt =
                        conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, publisher.name());
            stmt.setString(2, publisher.email().orElse(null));
            stmt.setString(3, publisher.phone().orElse(null));
            stmt.setString(4, publisher.address().orElse(null));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Creating publisher_id failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new Publisher(
                            generatedKeys.getLong(1),
                            publisher.name(),
                            publisher.email(),
                            publisher.phone(),
                            publisher.address());
                } else {
                    throw new DatabaseException("Creating publisher_id failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error saving publisher_id", e);
        }
    }

    private Publisher updatePublisher(Publisher publisher) {
        String sql =
                "UPDATE publisher SET name = ?, email = ?, phone = ?, address = ? WHERE publisher_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, publisher.name());
            stmt.setString(2, publisher.email().orElse(null));
            stmt.setString(3, publisher.phone().orElse(null));
            stmt.setString(4, publisher.address().orElse(null));
            stmt.setLong(5, publisher.id());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Updating publisher_id failed, no rows affected.");
            }
            return publisher;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating publisher_id", e);
        }
    }
}
