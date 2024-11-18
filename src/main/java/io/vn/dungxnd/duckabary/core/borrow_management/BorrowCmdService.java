package io.vn.dungxnd.duckabary.core.borrow_management;

import java.time.LocalDateTime;

public class BorrowCmdService extends BorrowService {

    public BorrowCmdService(BorrowManagement borrowManagement) {
        super(borrowManagement);
    }

    @Override
    public boolean borrowDocumentByUIdnDId(int userID, int documentID, LocalDateTime dueDate) {

        var user = userService.getUserByID(userID);
        var document = libraryService.getDocumentByID(documentID);

        if (user == null) {
            System.out.println("User with id " + userID + " not found");
            return false;
        }
        if (document == null) {
            System.out.println("Document with id " + documentID + " not found");
            return false;
        }
        if (document.getQuantity() <= 0) {
            System.out.println("Document with id " + documentID + " is out of stock");
            return false;
        }

        BorrowRecord borrowRecord =
                new BorrowRecord(
                        borrowDatabaseManagement.getNextRecordId(),
                        userID,
                        documentID,
                        LocalDateTime.now(),
                        dueDate);
        borrowManagement.saveBorrowRecord(borrowRecord);

        libraryService
                .getDocumentByID(documentID)
                .setQuantity(libraryService.getDocumentByID(documentID).getQuantity() - 1);

        System.out.println("Document borrowed successfully");
        return true;
    }

    @Override
    public boolean returnDocumentByUIdnDId(int userId, int docId) {
        BorrowRecord record = borrowDatabaseManagement.getBorrowRecord(userId, docId);
        if (record == null) {
            System.out.println(
                    "No borrow record found for user " + userId + " and document " + docId);
            return false;
        }
        if (userService.getUserByID(userId) == null) {
            System.out.println("User with id " + userId + " not found");
            return false;
        }
        if (libraryService.getDocumentByID(docId) == null) {
            System.out.println("Document with id " + docId + " not found");
            return false;
        }
        borrowManagement.returnDocument(record);

        libraryService
                .getDocumentByID(docId)
                .setQuantity(libraryService.getDocumentByID(docId).getQuantity() + 1);

        System.out.println("Document returned successfully");
        return true;
    }
}
