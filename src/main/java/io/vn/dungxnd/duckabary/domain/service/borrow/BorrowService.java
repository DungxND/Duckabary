package io.vn.dungxnd.duckabary.domain.service.borrow;

import io.vn.dungxnd.duckabary.domain.model.library.BorrowRecord;
import io.vn.dungxnd.duckabary.exception.DatabaseException;

import java.time.LocalDateTime;
import java.util.List;

public interface BorrowService {
    /**
     * Get all borrows records.
     *
     * @return List of all borrows.
     */
    List<BorrowRecord> getAllBorrows();

    /**
     * Get borrow record by id.
     *
     * @param id Borrow record id.
     * @return Borrow record.
     * @throws DatabaseException If borrow record not found.
     */
    BorrowRecord getBorrowById(int id) throws DatabaseException;

    /**
     * Borrow a document.
     *
     * @param userId User id.
     * @param documentId Document id.
     * @param quantity Quantity of document.
     * @param dueDate Due date.
     * @return Borrow record.
     * @throws DatabaseException If user, document not found or document not available.
     */
    BorrowRecord borrowDocument(int userId, Long documentId, int quantity, LocalDateTime dueDate)
            throws DatabaseException;

    /**
     * Return a document with borrow id.
     *
     * @param borrowId Borrow id.
     * @return Borrow record.
     * @throws DatabaseException If borrow record id not found.
     */
    BorrowRecord returnDocument(int borrowId) throws DatabaseException;

    /**
     * Get all borrows of a user.
     *
     * @param userId User id.
     * @return List of borrow records of the user.
     */
    List<BorrowRecord> getBorrowsByUser(int userId);

    /**
     * Get all borrows of a document.
     *
     * @param documentId Document id.
     * @return List of borrow records of the document.
     */
    List<BorrowRecord> getBorrowsByDocument(Long documentId);

    /**
     * Get all overdue borrows.
     *
     * @return List of overdue borrows.
     */
    List<BorrowRecord> getOverdueBorrows();

    /**
     * Get all active borrows.
     *
     * @return List of active borrows.
     */
    List<BorrowRecord> getActiveBorrows();

    /**
     * Check if document is available for borrow.
     *
     * @param documentId Document id.
     * @param quantity Quantity of document.
     * @return True if document is available for borrow.
     */
    boolean isDocumentAvailableForBorrow(Long documentId, int quantity);

    /**
     * Delete all borrows of a user.
     *
     * @param UID User id.
     */
    void deleteBorrowsByUser(int UID);
}
