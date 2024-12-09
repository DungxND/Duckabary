package io.vn.dungxnd.duckabary.infrastructure.repository.entity.impl;

import io.vn.dungxnd.duckabary.domain.database.DatabaseManager;
import io.vn.dungxnd.duckabary.domain.model.entity.Author;
import io.vn.dungxnd.duckabary.exception.DatabaseException;
import io.vn.dungxnd.duckabary.infrastructure.repository.entity.AuthorRepository;
import io.vn.dungxnd.duckabary.util.LoggerUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorRepositoryImpl implements AuthorRepository {
    private static final String SELECT_AUTHOR =
            "SELECT author_id, fullName, penName, email, phone, address, birthDate, deathDate FROM author";

    @Override
    public List<Author> getAll() {
        List<Author> authors = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_AUTHOR)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                authors.add(mapToAuthor(rs));
            }
            LoggerUtils.info("Retrieved " + authors.size() + " authors");
            return authors;
        } catch (SQLException e) {
            LoggerUtils.error("Failed to fetch all authors", e);
            throw new DatabaseException("Failed to fetch authors");
        }
    }

    @Override
    public Optional<Author> searchById(Long author_id) {
        String sql = SELECT_AUTHOR + " WHERE author_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, author_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToAuthor(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            LoggerUtils.error("Error finding author with id: " + author_id, e);
            throw new DatabaseException("Failed to fetch author by id");
        }
    }

    @Override
    public Optional<Author> searchByName(String name) {
        String sql = SELECT_AUTHOR + " WHERE fullName = ? OR penName = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToAuthor(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            LoggerUtils.error("Error finding author by name: " + name, e);
            throw new DatabaseException("Failed to fetch author by name");
        }
    }

    @Override
    public List<Author> searchByNamePattern(String name) {
        String sql = SELECT_AUTHOR + " WHERE fullName LIKE ? OR penName LIKE ?";
        List<Author> authors = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name + "%");
            stmt.setString(2, name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                authors.add(mapToAuthor(rs));
            }
            return authors;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding authors by name pattern: " + name, e);
            throw new DatabaseException("Failed to fetch authors by name pattern");
        }
    }

    @Override
    public Optional<Author> searchByEmail(String email) {
        String sql = SELECT_AUTHOR + " WHERE email = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToAuthor(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            LoggerUtils.error("Error finding author by email: " + email, e);
            throw new DatabaseException("Failed to fetch author by email");
        }
    }

    public List<Author> searchByEmailPattern(String emailPattern) {
        String sql = SELECT_AUTHOR + " WHERE email LIKE ?";
        List<Author> authors = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emailPattern + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                authors.add(mapToAuthor(rs));
            }
            return authors;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding authors by email pattern: " + emailPattern, e);
            throw new DatabaseException("Failed to fetch authors by email pattern");
        }
    }

    @Override
    public Optional<Author> searchByPhone(String phone) {
        String sql = SELECT_AUTHOR + " WHERE phone = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToAuthor(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            LoggerUtils.error("Error finding author by phone: " + phone, e);
            throw new DatabaseException("Failed to fetch author by phone");
        }
    }

    public List<Author> searchByPhonePattern(String phonePattern) {
        String sql = SELECT_AUTHOR + " WHERE phone LIKE ?";
        List<Author> authors = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phonePattern + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                authors.add(mapToAuthor(rs));
            }
            return authors;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding authors by phone pattern: " + phonePattern, e);
            throw new DatabaseException("Failed to fetch authors by phone pattern");
        }
    }

    @Override
    public Author save(Author author) {
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Author savedAuthor =
                        author.id() == null
                                ? insertAuthor(conn, author)
                                : updateAuthor(conn, author);
                conn.commit();
                return savedAuthor;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            LoggerUtils.error("Error saving author: " + author.fullName(), e);
            throw new DatabaseException("Failed to save author");
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM author WHERE author_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Author not found with id: " + id);
            }
        } catch (SQLException e) {
            LoggerUtils.error("Error deleting author with id: " + id, e);
            throw new DatabaseException("Failed to delete author");
        }
    }

    private Author mapToAuthor(ResultSet rs) throws SQLException {
        return new Author(
                rs.getLong("author_id"),
                rs.getString("fullName"),
                Optional.ofNullable(rs.getString("penName")),
                Optional.ofNullable(rs.getString("email")),
                Optional.ofNullable(rs.getString("phone")),
                Optional.ofNullable(rs.getString("address")),
                Optional.ofNullable(rs.getDate("birthDate")).map(Date::toLocalDate),
                Optional.ofNullable(rs.getDate("deathDate")).map(Date::toLocalDate));
    }

    private Author insertAuthor(Connection conn, Author author) throws SQLException {
        String sql =
                "INSERT INTO author (fullName, penName, email, phone, address, birthDate, deathDate) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, author.fullName());
            stmt.setString(2, author.penName().orElse(null));
            stmt.setString(3, author.email().orElse(null));
            stmt.setString(4, author.phone().orElse(null));
            stmt.setString(5, author.address().orElse(null));
            stmt.setDate(6, author.birthDate().map(Date::valueOf).orElse(null));
            stmt.setDate(7, author.deathDate().map(Date::valueOf).orElse(null));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating author failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new Author(
                            generatedKeys.getLong(1),
                            author.fullName(),
                            author.penName(),
                            author.email(),
                            author.phone(),
                            author.address(),
                            author.birthDate(),
                            author.deathDate());
                }
                throw new SQLException("Creating author failed, no ID obtained.");
            }
        }
    }

    private Author updateAuthor(Connection conn, Author author) throws SQLException {
        String sql =
                "UPDATE author SET fullName = ?, penName = ?, email = ?, phone = ?, "
                        + "address = ?, birthDate = ?, deathDate = ? WHERE author_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, author.fullName());
            stmt.setString(2, author.penName().orElse(null));
            stmt.setString(3, author.email().orElse(null));
            stmt.setString(4, author.phone().orElse(null));
            stmt.setString(5, author.address().orElse(null));
            stmt.setDate(6, author.birthDate().map(Date::valueOf).orElse(null));
            stmt.setDate(7, author.deathDate().map(Date::valueOf).orElse(null));
            stmt.setLong(8, author.id());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating author failed, no rows affected.");
            }
            return author;
        }
    }
}
