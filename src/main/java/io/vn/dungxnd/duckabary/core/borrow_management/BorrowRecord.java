package io.vn.dungxnd.duckabary.core.borrow_management;

import java.time.LocalDateTime;
import java.util.Optional;

public record BorrowRecord(
        int recordId,
        int userId,
        int documentId,
        int quantity,
        LocalDateTime borrowDate,
        LocalDateTime dueDate,
        Optional<LocalDateTime> returnDate) {

    public static BorrowRecord createBorrowRecord(
            int recordId,
            int userId,
            int documentId,
            int quantity,
            LocalDateTime borrowDate,
            LocalDateTime dueDate) {
        return new BorrowRecord(
                recordId, userId, documentId, quantity, borrowDate, dueDate, Optional.empty());
    }

    public static BorrowRecord createBorrowRecord(
            int recordId,
            int userId,
            int documentId,
            int quantity,
            LocalDateTime borrowDate,
            LocalDateTime dueDate,
            LocalDateTime returnDate) {
        return new BorrowRecord(
                recordId, userId, documentId, quantity, borrowDate, dueDate, Optional.of(returnDate));
    }

    public BorrowRecord returnDocument() {
        return new BorrowRecord(
                this.recordId,
                this.userId,
                this.documentId,
                this.quantity,
                this.borrowDate,
                this.dueDate,
                Optional.of(LocalDateTime.now()));
    }

    public boolean isReturned() {
        return returnDate.isPresent();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BorrowRecord that = (BorrowRecord) obj;
        return recordId == that.recordId;
    }
}
