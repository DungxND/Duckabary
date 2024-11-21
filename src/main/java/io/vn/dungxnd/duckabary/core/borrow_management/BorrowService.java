package io.vn.dungxnd.duckabary.core.borrow_management;

import io.vn.dungxnd.duckabary.core.library_management.LibraryService;
import io.vn.dungxnd.duckabary.core.user_management.UserService;
import io.vn.dungxnd.duckabary.utils.LoggerUtils;

import java.time.LocalDateTime;

public class BorrowService {

    protected final BorrowManagement borrowManagement;
    protected final BorrowDatabaseManagement borrowDatabaseManagement;
    protected final LibraryService libraryService;
    protected final UserService userService;

    public BorrowService(BorrowManagement borrowManagement) {
        this.borrowManagement = borrowManagement;
        this.borrowDatabaseManagement = new BorrowDatabaseManagement();
        this.libraryService = borrowManagement.getLibraryService();
        this.userService = borrowManagement.getUserService();
    }

    public boolean borrowDocumentByUIdnDId(int documentID, int userID, LocalDateTime dueDate) {
        var user = userService.getUserByID(userID);
        var document = libraryService.getDocumentByID(documentID);

        if (user == null || document == null || document.getQuantity() <= 0) {
            return false;
        }

        BorrowRecord borrowRecord =
                BorrowRecord.createBorrowRecord(
                        borrowDatabaseManagement.getNextRecordId(),
                        userID,
                        documentID,
                        LocalDateTime.now(),
                        dueDate);

        borrowManagement.saveBorrowRecord(borrowRecord);

        document.setQuantity(document.getQuantity() - 1);

        LoggerUtils.info("Document borrowed successfully");
        return true;
    }

    public boolean returnDocumentByUIdnDId(int userId, int docId) {
        BorrowRecord record = borrowDatabaseManagement.getBorrowRecord(userId, docId);
        var user = userService.getUserByID(userId);
        var document = libraryService.getDocumentByID(docId);

        if (record == null || user == null || document == null) {
            return false;
        }

        borrowManagement.returnDocument(record);

        document.setQuantity(document.getQuantity() + 1);

        LoggerUtils.info("Document returned successfully");
        return true;
    }

    public boolean returnDocumentByRecordId(int recordId) {
        BorrowRecord record = borrowManagement.getBorrowRecordByID(recordId);
        var user = userService.getUserByID(record.userId());
        var document = libraryService.getDocumentByID(record.documentId());

        if (user == null || document == null) {
            return false;
        }

        borrowManagement.returnDocument(record);

        document.setQuantity(document.getQuantity() + 1);

        LoggerUtils.info("Document returned successfully");
        return true;
    }
}
