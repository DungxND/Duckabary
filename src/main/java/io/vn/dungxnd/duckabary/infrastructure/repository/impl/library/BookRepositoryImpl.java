package io.vn.dungxnd.duckabary.infrastructure.repository.impl.library;

import io.vn.dungxnd.duckabary.domain.database.DatabaseManager;
import io.vn.dungxnd.duckabary.domain.model.library.Book;
import io.vn.dungxnd.duckabary.exeption.DatabaseException;
import io.vn.dungxnd.duckabary.infrastructure.repository.library.BookRepository;
import io.vn.dungxnd.duckabary.util.LoggerUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryImpl implements BookRepository {
    private static final String SELECT_BOOK =
            """
            SELECT d.*, b.isbn, b.publisher, b.language, b.genre
            FROM document d
            LEFT JOIN book b ON d.document_id = b.document_id
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

    private Long insertDocument(Connection conn, Book book) throws SQLException {
        String sql =
                """
            INSERT INTO document (title, author, description, publish_year,
            quantity, type, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, 'BOOK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
           """;
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, book.title());
            stmt.setString(2, book.author());
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

    private void insertBook(Connection conn, Long documentId, Book book) throws SQLException {
        String sql =
                "INSERT INTO book (document_id, isbn, publisher, language, genre) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, documentId);
            stmt.setString(2, book.isbn());
            stmt.setString(3, book.publisher());
            stmt.setString(4, book.language());
            stmt.setString(5, book.genre());
            stmt.executeUpdate();
        }
    }

    private void updateDocument(Connection conn, Book book) throws SQLException {
        String sql =
                """
                 UPDATE document SET title = ?, author = ?, description = ?,
                 publish_year = ?, quantity = ?, updated_at = CURRENT_TIMESTAMP
                 WHERE document_id = ?
                """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.title());
            stmt.setString(2, book.author());
            stmt.setString(3, book.description());
            stmt.setInt(4, book.publishYear());
            stmt.setInt(5, book.quantity());
            stmt.setLong(6, book.id());
            stmt.executeUpdate();
        }
    }

    private void updateBook(Connection conn, Book book) throws SQLException {
        String sql =
                "UPDATE book SET isbn = ?, publisher = ?, language = ?, genre = ? WHERE document_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.isbn());
            stmt.setString(2, book.publisher());
            stmt.setString(3, book.language());
            stmt.setString(4, book.genre());
            stmt.setLong(5, book.id());
            stmt.executeUpdate();
        }
    }

    private Book mapToBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getLong("document_id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("description"),
                rs.getInt("publish_year"),
                rs.getInt("quantity"),
                "BOOK",
                rs.getString("isbn"),
                rs.getString("publisher"),
                rs.getString("language"),
                rs.getString("genre"));
    }

    @Override
    public List<Book> findByTitle(String title) {
        String sql = SELECT_BOOK + " AND d.title LIKE ?";
        List<Book> books = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + title + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(mapToBook(rs));
            }
            LoggerUtils.info("Found " + books.size() + " books matching title: " + title);
            return books;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding books by title: " + title, e);
            throw new DatabaseException("Failed to find books by title");
        }
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        String sql = SELECT_BOOK + " AND b.isbn = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Book book = mapToBook(rs);
                LoggerUtils.info("Found book with ISBN: " + isbn);
                return Optional.of(book);
            }
            LoggerUtils.info("No book found with ISBN: " + isbn);
            return Optional.empty();
        } catch (SQLException e) {
            LoggerUtils.error("Error finding book by ISBN: " + isbn, e);
            throw new DatabaseException("Failed to find book by ISBN");
        }
    }

    @Override
    public List<Book> findByAuthor(String author) {
        String sql = SELECT_BOOK + " AND d.author LIKE ?";
        List<Book> books = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + author + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(mapToBook(rs));
            }
            LoggerUtils.info("Found " + books.size() + " books by author: " + author);
            return books;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding books by author: " + author, e);
            throw new DatabaseException("Failed to find books by author");
        }
    }
}
