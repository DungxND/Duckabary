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
            User user = userService.getUserByID(record.getUserId());
            if (user != null) {
                user.addBorrowRecord(record);
            }
        }
    }

    public List<BorrowRecord> getBorrowRecords() {
        return borrowRecords;
    }

    public void saveBorrowRecord(BorrowRecord record) {
        borrowRecords.add(record);

        borrowDatabaseManagement.saveBorrowRecord(record.getUserId(), record.getDocumentId());

        User user = userService.getUserByID(record.getUserId());
        if (user != null) {
            user.addBorrowRecord(record);
        }
    }

    public void returnDocument(BorrowRecord record) {
        borrowRecords.remove(record);

        borrowDatabaseManagement.returnDocument(
                record.getUserId(), record.getDocumentId(), record.getReturnDate());

        User user = userService.getUserByID(record.getUserId());
        if (user != null) {
            user.removeBorrowRecord(record);
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
            if (record.getRecordId() == recordId) {
                return record;
            }
        }
        return null;
    }

}
