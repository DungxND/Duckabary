package io.vn.dungxnd.duckabary.core.borrow_management;

import io.vn.dungxnd.duckabary.core.library_management.LibraryService;
import io.vn.dungxnd.duckabary.core.user_management.User;
import io.vn.dungxnd.duckabary.core.user_management.UserService;

import java.util.ArrayList;
import java.util.List;

public class BorrowManagement {
    private final BorrowDatabaseManagement borrowDatabaseManagement;
    private final LibraryService libraryService;
    private final UserService userService;
    private final List<BorrowRecord> borrowRecords = new ArrayList<>();

    public BorrowManagement(LibraryService libraryService) {
        this.borrowDatabaseManagement = new BorrowDatabaseManagement();
        this.libraryService = libraryService;
        this.userService = libraryService.getUserService();
        borrowRecords.addAll(borrowDatabaseManagement.getAllBorrowRecords());
        distributeBorrowRecordsToUsers();
    }

    private void distributeBorrowRecordsToUsers() {
        for (BorrowRecord record : borrowRecords) {
            User user = userService.getUserByID(record.userId());
            if (user != null) {
                user = user.addBorrowRecord(record);
                userService.updateUser(user);
            }
        }
    }

    public List<BorrowRecord> getBorrowRecords() {
        return borrowRecords;
    }

    public void saveBorrowRecord(BorrowRecord record) {
        borrowRecords.add(record);

        borrowDatabaseManagement.saveBorrowRecord(
                record.userId(),
                record.documentId(),
                record.quantity(),
                record.borrowDate(),
                record.dueDate(),
                record.returnDate().orElse(null));

        User user = userService.getUserByID(record.userId());
        if (user != null) {
            user = user.addBorrowRecord(record);
            userService.updateUser(user);
        }
    }

    public void returnDocument(BorrowRecord record) {
        BorrowRecord updatedRecord = record.returnDocument();

        int index = borrowRecords.indexOf(record);
        if (index != -1) {
            borrowRecords.set(index, updatedRecord);
        }

        borrowDatabaseManagement.returnDocument(
                updatedRecord.userId(),
                updatedRecord.documentId(),
                updatedRecord.returnDate().orElseThrow());

        User user = userService.getUserByID(updatedRecord.userId());
        if (user != null) {
            user = user.returnDocument(updatedRecord);
            userService.updateUser(user);
        }
    }

    public void cleanUpDatabaseBorrowRecord() {
        borrowDatabaseManagement.cleanUpRecords();
    }

    public LibraryService getLibraryService() {
        return libraryService;
    }

    public UserService getUserService() {
        return userService;
    }

    public BorrowRecord getBorrowRecordByID(int recordId) {
        for (BorrowRecord record : borrowRecords) {
            if (record.recordId() == recordId) {
                return record;
            }
        }
        return null;
    }
}
