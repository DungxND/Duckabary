package io.vn.dungxnd.duckabary.infrastructure.repository.library.impl;

import io.vn.dungxnd.duckabary.domain.database.DatabaseManager;
import io.vn.dungxnd.duckabary.domain.model.library.Book;
import io.vn.dungxnd.duckabary.exception.DatabaseException;
import io.vn.dungxnd.duckabary.infrastructure.repository.library.BookRepository;
import io.vn.dungxnd.duckabary.util.LoggerUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryImpl implements BookRepository {
    private static final String SELECT_BOOK =
            """
            SELECT d.*, b.isbn, b.publisher_id, b.language, b.genre,
                   a.fullName as author_name
            FROM document d
            LEFT JOIN book b ON d.document_id = b.document_id
            LEFT JOIN author a ON d.author_id = a.author_id
            WHERE d.type = 'BOOK'
            """;

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_BOOK)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(mapToBook(rs));
            }
            LoggerUtils.info("Retrieved " + books.size() + " books");
            return books;
        } catch (SQLException e) {
            LoggerUtils.error("Failed to fetch all books", e);
            throw new DatabaseException("Failed to fetch books");
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = SELECT_BOOK + " AND d.document_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToBook(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            LoggerUtils.error("Error finding book with id: " + id, e);
            throw new DatabaseException("Failed to fetch book by id");
        }
    }

    @Override
    public Book save(Book book) {
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Long documentId = book.id();
                if (documentId == null || documentId == 0) {
                    documentId = insertDocument(conn, book);
                    insertBook(conn, documentId, book);
                } else {
                    updateDocument(conn, book);
                    updateBook(conn, book);
                }
                conn.commit();
                return findById(documentId).orElseThrow();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            LoggerUtils.error("Error saving book: " + book.title(), e);
            throw new DatabaseException("Failed to save book");
        }
    }

    private void updateBook(Connection conn, Book book) {
        String sql =
                """
            UPDATE book
            SET isbn = ?, publisher_id = ?, language = ?, genre = ?
            WHERE document_id = ?
            """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.isbn());
            stmt.setLong(2, book.publisher_id());
            stmt.setString(3, book.language());
            stmt.setString(4, book.genre());
            stmt.setLong(5, book.id());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LoggerUtils.error("Error updating book: " + book.title(), e);
            throw new DatabaseException("Failed to update book");
        }
    }

    private void insertBook(Connection conn, Long documentId, Book book) {
        String sql =
                """
            INSERT INTO book (document_id, isbn, publisher_id, language, genre)
            VALUES (?, ?, ?, ?, ?)
            """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, documentId);
            stmt.setString(2, book.isbn());
            stmt.setLong(3, book.publisher_id());
            stmt.setString(4, book.language());
            stmt.setString(5, book.genre());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LoggerUtils.error("Error inserting book: " + book.title(), e);
            throw new DatabaseException("Failed to insert book");
        }
    }

    @Override
    public List<Book> findByTitle(String title) {
        return List.of();
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return Optional.empty();
    }

    private Long insertDocument(Connection conn, Book book) throws SQLException {
        String sql =
                """
            INSERT INTO document (title, author_id, description, publish_year,
            quantity, type, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, 'BOOK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            """;
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, book.title());
            stmt.setLong(2, book.author_id());
            stmt.setString(3, book.description());
            stmt.setInt(4, book.publishYear());
            stmt.setInt(5, book.quantity());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
            throw new SQLException("Failed to get generated document_id");
        }
    }

    private void updateDocument(Connection conn, Book book) throws SQLException {
        String sql =
                """
                UPDATE document
                SET title = ?, author_id = ?, description = ?,
                publish_year = ?, quantity = ?, updated_at = CURRENT_TIMESTAMP
                WHERE document_id = ?
                """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.title());
            stmt.setLong(2, book.author_id());
            stmt.setString(3, book.description());
            stmt.setInt(4, book.publishYear());
            stmt.setInt(5, book.quantity());
            stmt.setLong(6, book.id());
            stmt.executeUpdate();
        }
    }

    private Book mapToBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getLong("document_id"),
                rs.getString("title"),
                rs.getLong("author_id"),
                rs.getString("description"),
                rs.getInt("publish_year"),
                rs.getInt("quantity"),
                "BOOK",
                rs.getString("isbn"),
                rs.getLong("publisher_id"),
                rs.getString("language"),
                rs.getString("genre"));
    }

    @Override
    public List<Book> findByAuthorId(Long authorId) {
        String sql = SELECT_BOOK + " AND d.author_id = ?";
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, authorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(mapToBook(rs));
            }
            LoggerUtils.info("Found " + books.size() + " books by author ID: " + authorId);
            return books;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding books by author ID: " + authorId, e);
            throw new DatabaseException("Failed to find books by author");
        }
    }

    @Override
    public List<Book> findByPublisherId(Long publisherId) {
        String sql = SELECT_BOOK + " AND b.publisher_id = ?";
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, publisherId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(mapToBook(rs));
            }
            LoggerUtils.info("Found " + books.size() + " books by publisher_id ID: " + publisherId);
            return books;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding books by publisher_id ID: " + publisherId, e);
            throw new DatabaseException("Failed to find books by publisher_id");
        }
    }
}
