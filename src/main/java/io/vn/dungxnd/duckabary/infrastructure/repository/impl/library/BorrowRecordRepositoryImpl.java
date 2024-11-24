package io.vn.dungxnd.duckabary.infrastructure.repository.impl.library;

import io.vn.dungxnd.duckabary.domain.database.DatabaseManager;
import io.vn.dungxnd.duckabary.domain.model.library.BorrowRecord;
import io.vn.dungxnd.duckabary.exeption.DatabaseException;
import io.vn.dungxnd.duckabary.infrastructure.repository.library.BorrowRecordRepository;
import io.vn.dungxnd.duckabary.util.LoggerUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BorrowRecordRepositoryImpl implements BorrowRecordRepository {
    private static final String SELECT_BORROW =
            """
            SELECT borrow_id, user_id, document_id, borrow_quantity,
                   borrow_date, due_date, return_date
            FROM borrow
            """;

    @Override
    public List<BorrowRecord> findAll() {
        List<BorrowRecord> records = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_BORROW)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                records.add(mapToBorrowRecord(rs));
            }
            LoggerUtils.info("Retrieved " + records.size() + " borrow records");
            return records;
        } catch (SQLException e) {
            LoggerUtils.error("Failed to fetch all borrow records", e);
            throw new DatabaseException("Failed to fetch borrow records");
        }
    }

    @Override
    public Optional<BorrowRecord> findById(int id) {
        String sql = SELECT_BORROW + " WHERE borrow_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToBorrowRecord(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            LoggerUtils.error("Error finding borrow record with id: " + id, e);
            throw new DatabaseException("Failed to fetch borrow record by id");
        }
    }

    @Override
    public BorrowRecord save(BorrowRecord record) {
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                if (record.recordId() == 0) {
                    return insertBorrowRecord(conn, record);
                } else {
                    return updateBorrowRecord(conn, record);
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            LoggerUtils.error("Error saving borrow record", e);
            throw new DatabaseException("Failed to save borrow record");
        }
    }

    private BorrowRecord updateBorrowRecord(Connection conn, BorrowRecord record)
            throws SQLException {
        String sql =
                """
        UPDATE borrow
        SET user_id = ?, document_id = ?, borrow_quantity = ?,
            borrow_date = ?, due_date = ?, return_date = ?
        WHERE borrow_id = ?
       """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, record.userId());
            stmt.setLong(2, record.documentId());
            stmt.setInt(3, record.quantity());
            stmt.setTimestamp(4, Timestamp.valueOf(record.borrowDate()));
            stmt.setTimestamp(5, Timestamp.valueOf(record.dueDate()));
            record.returnDate()
                    .ifPresentOrElse(date -> setTimestamp(stmt, 6, date), () -> setNull(stmt, 6));
            stmt.setInt(7, record.recordId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating borrow record failed, no rows affected.");
            }

            return findById(record.recordId()).orElseThrow();
        }
    }

    @Override
    public void delete(int id) {}

    @Override
    public List<BorrowRecord> findByUserId(int userId) {
        String sql = SELECT_BORROW + " WHERE user_id = ?";
        List<BorrowRecord> records = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                records.add(mapToBorrowRecord(rs));
            }
            return records;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding borrow records for user: " + userId, e);
            throw new DatabaseException("Failed to find borrow records by user");
        }
    }

    @Override
    public List<BorrowRecord> findByDocumentId(Long documentId) {
        String sql = SELECT_BORROW + " WHERE document_id = ?";
        List<BorrowRecord> records = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, documentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                records.add(mapToBorrowRecord(rs));
            }
            return records;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding borrow records for document: " + documentId, e);
            throw new DatabaseException("Failed to find borrow records by document");
        }
    }

    @Override
    public List<BorrowRecord> findOverdueRecords() {
        String sql = SELECT_BORROW + " WHERE return_date IS NULL AND due_date < CURRENT_TIMESTAMP";
        List<BorrowRecord> records = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                records.add(mapToBorrowRecord(rs));
            }
            return records;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding overdue records", e);
            throw new DatabaseException("Failed to find overdue records");
        }
    }

    @Override
    public List<BorrowRecord> findActiveRecords() {
        String sql = SELECT_BORROW + " WHERE return_date IS NULL";
        List<BorrowRecord> records = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                records.add(mapToBorrowRecord(rs));
            }
            return records;
        } catch (SQLException e) {
            LoggerUtils.error("Error finding active records", e);
            throw new DatabaseException("Failed to find active records");
        }
    }

    private BorrowRecord mapToBorrowRecord(ResultSet rs) throws SQLException {
        Timestamp returnDate = rs.getTimestamp("return_date");
        if (returnDate != null) {
            return BorrowRecord.createBorrowRecord(
                    rs.getInt("borrow_id"),
                    rs.getInt("user_id"),
                    rs.getLong("document_id"),
                    rs.getInt("borrow_quantity"),
                    rs.getTimestamp("borrow_date").toLocalDateTime(),
                    rs.getTimestamp("due_date").toLocalDateTime(),
                    returnDate.toLocalDateTime());
        } else {
            return BorrowRecord.createBorrowRecord(
                    rs.getInt("borrow_id"),
                    rs.getInt("user_id"),
                    rs.getLong("document_id"),
                    rs.getInt("borrow_quantity"),
                    rs.getTimestamp("borrow_date").toLocalDateTime(),
                    rs.getTimestamp("due_date").toLocalDateTime());
        }
    }

    private BorrowRecord insertBorrowRecord(Connection conn, BorrowRecord record)
            throws SQLException {
        String sql =
                """
            INSERT INTO borrow (user_id, document_id, borrow_quantity,
            borrow_date, due_date, return_date)
            VALUES (?, ?, ?, ?, ?, ?)
           """;
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, record.userId());
            stmt.setLong(2, record.documentId());
            stmt.setInt(3, record.quantity());
            stmt.setTimestamp(4, Timestamp.valueOf(record.borrowDate()));
            stmt.setTimestamp(5, Timestamp.valueOf(record.dueDate()));
            record.returnDate()
                    .ifPresentOrElse(date -> setTimestamp(stmt, 6, date), () -> setNull(stmt, 6));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating borrow record failed, no rows affected.");
            }

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return findById(generatedKeys.getInt(1)).orElseThrow();
            }
            throw new SQLException("Creating borrow record failed, no ID obtained.");
        }
    }

    private void setTimestamp(PreparedStatement stmt, int parameterIndex, LocalDateTime dateTime) {
        try {
            stmt.setTimestamp(parameterIndex, Timestamp.valueOf(dateTime));
        } catch (SQLException e) {
            throw new DatabaseException("Error setting timestamp", e);
        }
    }

    private void setNull(PreparedStatement stmt, int parameterIndex) {
        try {
            stmt.setNull(parameterIndex, Types.TIMESTAMP);
        } catch (SQLException e) {
            throw new DatabaseException("Error setting null timestamp", e);
        }
    }
}
