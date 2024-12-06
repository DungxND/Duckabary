package io.vn.dungxnd.duckabary.infrastructure.repository.library.impl;

import io.vn.dungxnd.duckabary.domain.database.DatabaseManager;
import io.vn.dungxnd.duckabary.domain.model.library.Journal;
import io.vn.dungxnd.duckabary.exception.DatabaseException;
import io.vn.dungxnd.duckabary.infrastructure.repository.library.JournalRepository;
import io.vn.dungxnd.duckabary.util.LoggerUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JournalRepositoryImpl implements JournalRepository {
    private static final String SELECT_JOURNAL =
            """
            SELECT d.*, j.issn, j.volume, j.issue
            FROM document d
            LEFT JOIN journal j ON d.document_id = j.document_id
            WHERE d.type = 'JOURNAL'
            """;

    @Override
    public List<Journal> getAll() {
        List<Journal> journals = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_JOURNAL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                journals.add(mapToJournal(rs));
            }
            LoggerUtils.info("Retrieved " + journals.size() + " journals");
            return journals;
        } catch (SQLException e) {
            LoggerUtils.error("Failed to fetch all journals", e);
            throw new DatabaseException("Failed to fetch journals");
        }
    }

    @Override
    public Optional<Journal> searchById(Long id) {
        String sql = SELECT_JOURNAL + " AND d.document_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToJournal(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            LoggerUtils.error("Error finding journal with id: " + id, e);
            throw new DatabaseException("Failed to fetch journal by id");
        }
    }

    @Override
    public Journal save(Journal journal) {
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Long documentId = journal.id();
                if (documentId == null || documentId == 0) {
                    documentId = insertDocument(conn, journal);
                    insertJournal(conn, documentId, journal);
                } else {
                    updateDocument(conn, journal);
                    updateJournal(conn, journal);
                }
                conn.commit();
                return searchById(documentId).orElseThrow();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            LoggerUtils.error("Error saving journal: " + journal.title(), e);
            throw new DatabaseException("Failed to save journal");
        }
    }

    @Override
    public List<Journal> searchByTitle(String title) {
        String sql = SELECT_JOURNAL + " AND d.title LIKE ?";
        List<Journal> journals = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + title + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                journals.add(mapToJournal(rs));
            }
            LoggerUtils.info("Found " + journals.size() + " journals matching title: " + title);
            return journals;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding journals by title: " + title, e);
            throw new DatabaseException("Failed to find journals by title");
        }
    }

    @Override
    public Optional<Journal> searchByIssn(String issn) {
        String sql = SELECT_JOURNAL + " AND j.issn = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, issn);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToJournal(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            LoggerUtils.error("Error finding journal by ISSN: " + issn, e);
            throw new DatabaseException("Failed to find journal by ISSN");
        }
    }

    @Override
    public List<Journal> searchByAuthorId(Long authorId) {
        String sql = SELECT_JOURNAL + " AND d.author_id = ?";
        List<Journal> journals = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, authorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                journals.add(mapToJournal(rs));
            }
            LoggerUtils.info("Found " + journals.size() + " journals by author ID: " + authorId);
            return journals;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding journals by author ID: " + authorId, e);
            throw new DatabaseException("Failed to find journals by author");
        }
    }

    private Long insertDocument(Connection conn, Journal journal) throws SQLException {
        String sql =
                """
            INSERT INTO document (title, author_id, description, publish_year,
            quantity, type, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, 'JOURNAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            """;
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, journal.title());
            stmt.setLong(2, journal.author_id());
            stmt.setString(3, journal.description());
            stmt.setInt(4, journal.publishYear());
            stmt.setInt(5, journal.quantity());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
            throw new SQLException("Failed to get generated document_id");
        }
    }

    private void insertJournal(Connection conn, Long documentId, Journal journal)
            throws SQLException {
        String sql = "INSERT INTO journal (document_id, issn, volume, issue) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, documentId);
            stmt.setString(2, journal.issn());
            stmt.setString(3, journal.volume());
            stmt.setString(4, journal.issue());
            stmt.executeUpdate();
        }
    }

    private void updateDocument(Connection conn, Journal journal) throws SQLException {
        String sql =
                """
            UPDATE document
            SET title = ?, author_id = ?, description = ?,
            publish_year = ?, quantity = ?, updated_at = CURRENT_TIMESTAMP
            WHERE document_id = ?
            """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, journal.title());
            stmt.setLong(2, journal.author_id());
            stmt.setString(3, journal.description());
            stmt.setInt(4, journal.publishYear());
            stmt.setInt(5, journal.quantity());
            stmt.setLong(6, journal.id());
            stmt.executeUpdate();
        }
    }

    private void updateJournal(Connection conn, Journal journal) throws SQLException {
        String sql = "UPDATE journal SET issn = ?, volume = ?, issue = ? WHERE document_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, journal.issn());
            stmt.setString(2, journal.volume());
            stmt.setString(3, journal.issue());
            stmt.setLong(4, journal.id());
            stmt.executeUpdate();
        }
    }

    private Journal mapToJournal(ResultSet rs) throws SQLException {
        return new Journal(
                rs.getLong("document_id"),
                rs.getString("title"),
                rs.getLong("author_id"),
                rs.getString("description"),
                rs.getInt("publish_year"),
                rs.getInt("quantity"),
                "JOURNAL",
                rs.getString("issn"),
                rs.getString("volume"),
                rs.getString("issue"));
    }
}
