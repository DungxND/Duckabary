package io.vn.dungxnd.duckabary.core.borrow_management;

import java.time.LocalDateTime;

public class BorrowRecord {
    private int recordId;
    private int userId;
    private int documentId;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private boolean isReturned;

    public BorrowRecord(
            int recordId,
            int userId,
            int documentId,
            LocalDateTime borrowDate,
            LocalDateTime dueDate) {
        this.recordId = recordId;
        this.userId = userId;
        this.documentId = documentId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.isReturned = false;
    }

    public BorrowRecord(
            int recordId,
            int userId,
            int documentId,
            LocalDateTime borrowDate,
            LocalDateTime dueDate,
            LocalDateTime returnDate,
            boolean isReturned) {
        this.recordId = recordId;
        this.userId = userId;
        this.documentId = documentId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.isReturned = isReturned;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    public void returnDocument() {
        this.isReturned = true;
        this.returnDate = LocalDateTime.now();
    }
}
