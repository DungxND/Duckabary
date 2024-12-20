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
                   t.university, t.department, t.supervisor, t.degree, t.defense_date,
                   a.fullName, a.penName
            FROM document d
            INNER JOIN author a ON d.author_id = a.author_id
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
    public Optional<Document> checkExistByIdentifier(String identifier) {
        String sql = SELECT_DOCUMENT + " WHERE b.isbn = ? OR j.issn = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, identifier);
            stmt.setString(2, identifier);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToDocument(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            LoggerUtils.error("Error finding document with identifier: " + identifier, e);
            throw new DatabaseException("Failed to fetch document by identifier");
        }
    }

    @Override
    public Optional<Document> searchById(Long id) {
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
    public List<Document> searchByTitle(String title) {
        String sql = SELECT_DOCUMENT + " WHERE LOWER(d.title) LIKE LOWER(?)";
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

    public List<Document> searchByGenre(String genre) {
        String sql = SELECT_DOCUMENT + " WHERE LOWER(b.genre) = LOWER(?)";
        List<Document> documents = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, genre);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                documents.add(mapToDocument(rs));
            }
            LoggerUtils.info("Found " + documents.size() + " documents of genre: " + genre);
            return documents;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding documents by genre: " + genre, e);
            throw new DatabaseException("Failed to find documents by genre");
        }
    }

    @Override
    public List<Document> searchByAuthorId(Long authorId) {
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
    public List<Document> searchByAuthorName(String nameOrPenName) {
        String sql =
                SELECT_DOCUMENT
                        + " WHERE LOWER(TRIM(CAST(a.fullName AS TEXT))) LIKE LOWER(?) OR "
                        + "(a.penName IS NOT NULL AND LOWER(TRIM(CAST(a.penName AS TEXT))) LIKE LOWER(?))";

        List<Document> documents = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchTerm = "%" + nameOrPenName.toLowerCase().trim() + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);

            LoggerUtils.info("Executing search with term: " + searchTerm);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                documents.add(mapToDocument(rs));
            }
            return documents;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding documents by author name: " + nameOrPenName, e);
            throw new DatabaseException("Failed to find documents by author");
        }
    }

    private void debugPrintTableContents(Connection conn, String tableName) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + tableName);
                ResultSet rs = stmt.executeQuery()) {
            LoggerUtils.info("Contents of " + tableName + " table:");
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                StringBuilder row = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    row.append(metaData.getColumnName(i))
                            .append(": ")
                            .append(rs.getString(i))
                            .append(" | ");
                }
                LoggerUtils.info(row.toString());
            }
        } catch (SQLException e) {
            LoggerUtils.error("Error printing table " + tableName + ": ", e);
        }
    }

    @Override
    public List<Document> searchByType(String type) {
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
