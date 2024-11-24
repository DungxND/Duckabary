package io.vn.dungxnd.duckabary.domain.service.borrow;

import io.vn.dungxnd.duckabary.domain.model.library.BorrowRecord;
import io.vn.dungxnd.duckabary.exeption.DatabaseException;

import java.time.LocalDateTime;
import java.util.List;

public interface BorrowService {
    List<BorrowRecord> getAllBorrows();

    BorrowRecord getBorrowById(int id) throws DatabaseException;

    BorrowRecord borrowDocument(int userId, Long documentId, int quantity, LocalDateTime dueDate)
            throws DatabaseException;

    BorrowRecord returnDocument(int borrowId) throws DatabaseException;

    List<BorrowRecord> getBorrowsByUser(int userId);

    List<BorrowRecord> getBorrowsByDocument(Long documentId);

    List<BorrowRecord> getOverdueBorrows();

    List<BorrowRecord> getActiveBorrows();

    boolean isDocumentAvailableForBorrow(Long documentId, int quantity);
}
