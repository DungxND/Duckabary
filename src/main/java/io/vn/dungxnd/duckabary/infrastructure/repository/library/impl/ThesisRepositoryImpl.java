package io.vn.dungxnd.duckabary.infrastructure.repository.library.impl;

import io.vn.dungxnd.duckabary.domain.database.DatabaseManager;
import io.vn.dungxnd.duckabary.domain.model.library.Thesis;
import io.vn.dungxnd.duckabary.exception.DatabaseException;
import io.vn.dungxnd.duckabary.infrastructure.repository.library.ThesisRepository;
import io.vn.dungxnd.duckabary.util.LoggerUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ThesisRepositoryImpl implements ThesisRepository {
    private static final String SELECT_THESIS =
            """
            SELECT d.*, t.university, t.department, t.supervisor, t.degree, t.defense_date
            FROM document d
            LEFT JOIN thesis t ON d.document_id = t.document_id
            WHERE d.type = 'THESIS'
            """;

    @Override
    public List<Thesis> findAll() {
        List<Thesis> theses = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_THESIS)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                theses.add(mapToThesis(rs));
            }
            LoggerUtils.info("Retrieved " + theses.size() + " theses");
            return theses;
        } catch (SQLException e) {
            LoggerUtils.error("Failed to fetch all theses", e);
            throw new DatabaseException("Failed to fetch theses");
        }
    }

    @Override
    public Optional<Thesis> findById(Long id) {
        String sql = SELECT_THESIS + " AND d.document_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToThesis(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            LoggerUtils.error("Error finding thesis with id: " + id, e);
            throw new DatabaseException("Failed to fetch thesis by id");
        }
    }

    @Override
    public Thesis save(Thesis thesis) {
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Long documentId = thesis.id();
                if (documentId == null || documentId == 0) {
                    documentId = insertDocument(conn, thesis);
                    insertThesis(conn, documentId, thesis);
                } else {
                    updateDocument(conn, thesis);
                    updateThesis(conn, thesis);
                }
                conn.commit();
                return findById(documentId).orElseThrow();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            LoggerUtils.error("Error saving thesis: " + thesis.title(), e);
            throw new DatabaseException("Failed to save thesis");
        }
    }

    @Override
    public List<Thesis> findByTitle(String title) {
        String sql = SELECT_THESIS + " AND d.title LIKE ?";
        List<Thesis> theses = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + title + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                theses.add(mapToThesis(rs));
            }
            LoggerUtils.info("Found " + theses.size() + " theses matching title: " + title);
            return theses;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding theses by title: " + title, e);
            throw new DatabaseException("Failed to find theses by title");
        }
    }

    @Override
    public List<Thesis> findByAuthorId(Long authorId) {
        String sql = SELECT_THESIS + " AND d.author_id = ?";
        List<Thesis> theses = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, authorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                theses.add(mapToThesis(rs));
            }
            LoggerUtils.info("Found " + theses.size() + " theses by author id: " + authorId);
            return theses;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding theses by author id: " + authorId, e);
            throw new DatabaseException("Failed to find theses by author id");
        }
    }

    @Override
    public List<Thesis> findByUniversity(String university) {
        String sql = SELECT_THESIS + " AND t.university LIKE ?";
        List<Thesis> theses = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + university + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                theses.add(mapToThesis(rs));
            }
            LoggerUtils.info("Found " + theses.size() + " theses from university: " + university);
            return theses;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding theses by university: " + university, e);
            throw new DatabaseException("Failed to find theses by university");
        }
    }

    @Override
    public List<Thesis> findBySupervisor(String supervisor) {
        String sql = SELECT_THESIS + " AND t.supervisor LIKE ?";
        List<Thesis> theses = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + supervisor + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                theses.add(mapToThesis(rs));
            }
            LoggerUtils.info("Found " + theses.size() + " theses by supervisor: " + supervisor);
            return theses;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding theses by supervisor: " + supervisor, e);
            throw new DatabaseException("Failed to find theses by supervisor");
        }
    }

    private Long insertDocument(Connection conn, Thesis thesis) throws SQLException {
        String sql =
                """
            INSERT INTO document (title, author_id, description, publish_year,
            quantity, type, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, 'THESIS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            """;
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, thesis.title());
            stmt.setLong(2, thesis.author_id());
            stmt.setString(3, thesis.description());
            stmt.setInt(4, thesis.publishYear());
            stmt.setInt(5, thesis.quantity());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
            throw new SQLException("Failed to get generated document_id");
        }
    }

    private void insertThesis(Connection conn, Long documentId, Thesis thesis) throws SQLException {
        String sql =
                """
            INSERT INTO thesis (document_id, university, department, supervisor,
            degree, defense_date)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, documentId);
            stmt.setString(2, thesis.university());
            stmt.setString(3, thesis.department());
            stmt.setString(4, thesis.supervisor());
            stmt.setString(5, thesis.degree());
            stmt.setTimestamp(6, thesis.defenseDate().map(Timestamp::valueOf).orElse(null));
            stmt.executeUpdate();
        }
    }

    private void updateDocument(Connection conn, Thesis thesis) throws SQLException {
        String sql =
                """
            UPDATE document
            SET title = ?, author_id = ?, description = ?,
            publish_year = ?, quantity = ?, updated_at = CURRENT_TIMESTAMP
            WHERE document_id = ?
            """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, thesis.title());
            stmt.setLong(2, thesis.author_id());
            stmt.setString(3, thesis.description());
            stmt.setInt(4, thesis.publishYear());
            stmt.setInt(5, thesis.quantity());
            stmt.setLong(6, thesis.id());
            stmt.executeUpdate();
        }
    }

    private void updateThesis(Connection conn, Thesis thesis) throws SQLException {
        String sql =
                """
            UPDATE thesis
            SET university = ?, department = ?, supervisor = ?,
            degree = ?, defense_date = ?
            WHERE document_id = ?
            """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, thesis.university());
            stmt.setString(2, thesis.department());
            stmt.setString(3, thesis.supervisor());
            stmt.setString(4, thesis.degree());
            stmt.setTimestamp(5, thesis.defenseDate().map(Timestamp::valueOf).orElse(null));
            stmt.setLong(6, thesis.id());
            stmt.executeUpdate();
        }
    }

    private Thesis mapToThesis(ResultSet rs) throws SQLException {
        return new Thesis(
                rs.getLong("document_id"),
                rs.getString("title"),
                rs.getLong("author_id"),
                rs.getString("description"),
                rs.getInt("publish_year"),
                rs.getInt("quantity"),
                "THESIS",
                rs.getString("university"),
                rs.getString("department"),
                rs.getString("supervisor"),
                rs.getString("degree"),
                Optional.ofNullable(rs.getTimestamp("defense_date"))
                        .map(Timestamp::toLocalDateTime));
    }
}
