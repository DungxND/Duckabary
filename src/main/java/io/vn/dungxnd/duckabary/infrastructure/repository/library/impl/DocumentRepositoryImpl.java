package io.vn.dungxnd.duckabary.infrastructure.repository.library.impl;

import io.vn.dungxnd.duckabary.domain.database.DatabaseManager;
import io.vn.dungxnd.duckabary.domain.model.library.Book;
import io.vn.dungxnd.duckabary.domain.model.library.Document;
import io.vn.dungxnd.duckabary.domain.model.library.Journal;
import io.vn.dungxnd.duckabary.domain.model.library.Thesis;
import io.vn.dungxnd.duckabary.exception.DatabaseException;
import io.vn.dungxnd.duckabary.infrastructure.repository.library.DocumentRepository;
import io.vn.dungxnd.duckabary.util.LoggerUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DocumentRepositoryImpl implements DocumentRepository {
    private static final String SELECT_DOCUMENT =
            """
            SELECT d.*, b.isbn, b.publisher_id, b.language, b.genre,
                   j.issn, j.volume, j.issue,
                   t.university, t.department, t.supervisor, t.degree, t.defense_date
            FROM document d
            LEFT JOIN book b ON d.document_id = b.document_id
            LEFT JOIN journal j ON d.document_id = j.document_id
            LEFT JOIN thesis t ON d.document_id = t.document_id
            """;

    @Override
    public List<Document> findAll() {
        List<Document> documents = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_DOCUMENT)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                documents.add(mapToDocument(rs));
            }
            LoggerUtils.info("Retrieved " + documents.size() + " documents");
            return documents;
        } catch (SQLException e) {
            LoggerUtils.error("Failed to fetch all documents", e);
            throw new DatabaseException("Failed to fetch documents");
        }
    }

    @Override
    public Optional<Document> findById(Long id) {
        String sql = SELECT_DOCUMENT + " WHERE d.document_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToDocument(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            LoggerUtils.error("Error finding document with id: " + id, e);
            throw new DatabaseException("Failed to fetch document by id");
        }
    }

    @Override
    public List<Document> findByTitle(String title) {
        String sql = SELECT_DOCUMENT + " WHERE d.title LIKE ?";
        List<Document> documents = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + title + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                documents.add(mapToDocument(rs));
            }
            LoggerUtils.info("Found " + documents.size() + " documents matching title: " + title);
            return documents;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding documents by title: " + title, e);
            throw new DatabaseException("Failed to find documents by title");
        }
    }

    @Override
    public List<Document> findByAuthorId(Long authorId) {
        String sql = SELECT_DOCUMENT + " WHERE d.author_id = ?";
        List<Document> documents = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, authorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                documents.add(mapToDocument(rs));
            }
            LoggerUtils.info("Found " + documents.size() + " documents by author id: " + authorId);
            return documents;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding documents by author id: " + authorId, e);
            throw new DatabaseException("Failed to find documents by author");
        }
    }

    @Override
    public List<Document> findByAuthorName(String nameOrPenName) {
        String sql =
                SELECT_DOCUMENT
                        + " WHERE d.author_id IN (SELECT author.author_id FROM author WHERE fullName LIKE ? OR penName LIKE ?)";
        List<Document> documents = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nameOrPenName + "%");
            stmt.setString(2, "%" + nameOrPenName + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                documents.add(mapToDocument(rs));
            }
            LoggerUtils.info(
                    "Found " + documents.size() + " documents by author name: " + nameOrPenName);
            return documents;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding documents by author name: " + nameOrPenName, e);
            throw new DatabaseException("Failed to find documents by author");
        }
    }

    @Override
    public List<Document> findByType(String type) {
        String sql = SELECT_DOCUMENT + " WHERE d.type = ?";
        List<Document> documents = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                documents.add(mapToDocument(rs));
            }
            LoggerUtils.info("Found " + documents.size() + " documents of type: " + type);
            return documents;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding documents by type: " + type, e);
            throw new DatabaseException("Failed to find documents by type");
        }
    }

    @Override
    public int countByType(String type) {
        String sql = "SELECT COUNT(*) FROM document WHERE type = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            LoggerUtils.error("Error counting documents by type: " + type, e);
            throw new DatabaseException("Failed to count documents by type");
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                String[] deleteSpecificSql = {
                    "DELETE FROM book WHERE document_id = ?",
                    "DELETE FROM journal WHERE document_id = ?",
                    "DELETE FROM thesis WHERE document_id = ?"
                };
                for (String sql : deleteSpecificSql) {
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setLong(1, id);
                        stmt.executeUpdate();
                    }
                }
                String deleteDocumentSql = "DELETE FROM document WHERE document_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(deleteDocumentSql)) {
                    stmt.setLong(1, id);
                    int affectedRows = stmt.executeUpdate();
                    if (affectedRows == 0) {
                        throw new DatabaseException("Document not found with id: " + id);
                    }
                }
                conn.commit();
                LoggerUtils.info("Deleted document with id: " + id);
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            LoggerUtils.error("Error deleting document with id: " + id, e);
            throw new DatabaseException("Failed to delete document");
        }
    }

    private Document mapToDocument(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        return switch (type) {
            case "BOOK" ->
                    new Book(
                            rs.getLong("document_id"),
                            rs.getString("title"),
                            rs.getLong("author_id"),
                            rs.getString("description"),
                            rs.getInt("publish_year"),
                            rs.getInt("quantity"),
                            type,
                            rs.getString("isbn"),
                            rs.getLong("publisher_id"),
                            rs.getString("language"),
                            rs.getString("genre"));
            case "JOURNAL" ->
                    new Journal(
                            rs.getLong("document_id"),
                            rs.getString("title"),
                            rs.getLong("author_id"),
                            rs.getString("description"),
                            rs.getInt("publish_year"),
                            rs.getInt("quantity"),
                            type,
                            rs.getString("issn"),
                            rs.getString("volume"),
                            rs.getString("issue"));
            case "THESIS" ->
                    new Thesis(
                            rs.getLong("document_id"),
                            rs.getString("title"),
                            rs.getLong("author_id"),
                            rs.getString("description"),
                            rs.getInt("publish_year"),
                            rs.getInt("quantity"),
                            type,
                            rs.getString("university"),
                            rs.getString("department"),
                            rs.getString("supervisor"),
                            rs.getString("degree"),
                            Optional.ofNullable(rs.getTimestamp("defense_date"))
                                    .map(Timestamp::toLocalDateTime));
            default -> throw new DatabaseException("Unknown document type: " + type);
        };
    }
}
