package io.vn.dungxnd.duckabary.core.borrow_management;

import io.vn.dungxnd.duckabary.db.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BorrowDatabaseManagement {

    public List<BorrowRecord> getAllBorrowRecords() {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        String sql = "SELECT * FROM borrow";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int borrowId = rs.getInt("borrow_id");
                int userId = rs.getInt("user_id");
                int documentId = rs.getInt("document_id");
                LocalDateTime borrowDate = rs.getTimestamp("borrow_date").toLocalDateTime();
                LocalDateTime dueDate = rs.getTimestamp("due_date").toLocalDateTime();
                LocalDateTime returnDate =
                        rs.getTimestamp("return_date") != null
                                ? rs.getTimestamp("return_date").toLocalDateTime()
                                : null;
                boolean isReturned = rs.getBoolean("is_returned");
                BorrowRecord record =
                        new BorrowRecord(
                                borrowId,
                                userId,
                                documentId,
                                borrowDate,
                                dueDate,
                                returnDate,
                                isReturned);
                borrowRecords.add(record);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching borrow records: " + e.getMessage());
        }
        System.out.println("Loaded " + borrowRecords.size() + " borrow records.");
        return borrowRecords;
    }

    public void saveBorrowRecord(int userId, int docId) {
        String sql = "INSERT INTO borrow(user_id, document_id) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, docId);
            pstmt.executeUpdate();
            System.out.println("Document borrowed successfully.");
        } catch (SQLException e) {
            System.out.println("Error borrowing document: " + e.getMessage());
        }
    }

    public int getNextRecordId() {
        String sql = "SELECT MAX(borrow_id) FROM borrow";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            return pstmt.executeQuery().getInt(1) + 1;
        } catch (SQLException e) {
            System.out.println("Error getting next record id: " + e.getMessage());
            return -1;
        }
    }

    public BorrowRecord getBorrowRecord(int userId, int docId) {
        String sql = "SELECT * FROM borrow WHERE user_id = ? AND document_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, docId);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int borrowId = rs.getInt("borrow_id");
                LocalDateTime borrowDate = rs.getTimestamp("borrow_date").toLocalDateTime();
                LocalDateTime dueDate = rs.getTimestamp("due_date").toLocalDateTime();
                return new BorrowRecord(borrowId, userId, docId, borrowDate, dueDate);
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Error getting borrow record: " + e.getMessage());
            return null;
        }
    }

    public void updateDueDate(int userId, int docId, LocalDateTime dueDate) {
        String sql = "UPDATE borrow SET due_date = ? WHERE user_id = ? AND document_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, java.sql.Timestamp.valueOf(dueDate));
            pstmt.setInt(2, userId);
            pstmt.setInt(3, docId);
            pstmt.executeUpdate();
            System.out.println("Due date updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating due date: " + e.getMessage());
        }
    }

    public void returnDocument(int userId, int documentId, LocalDateTime returnDate) {
        String sql =
                "UPDATE borrow SET return_date = ?, is_returned = ? WHERE user_id = ? AND document_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, java.sql.Timestamp.valueOf(returnDate));
            pstmt.setBoolean(2, true);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, documentId);
            pstmt.executeUpdate();
            System.out.println("Document returned successfully.");
        } catch (SQLException e) {
            System.out.println("Error returning document: " + e.getMessage());
        }
    }

    public void cleanUpRecords() {

        // TODO: Clean up records, update user's Borrow Record array borrow id

        String sql = "DELETE FROM borrow WHERE is_returned = true";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
            System.out.println("Cleaned up records successfully.");
        } catch (SQLException e) {
            System.out.println("Error cleaning up records: " + e.getMessage());
        }
    }
}
