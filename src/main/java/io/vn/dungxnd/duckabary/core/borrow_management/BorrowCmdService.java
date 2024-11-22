package io.vn.dungxnd.duckabary.core.borrow_management;

import io.vn.dungxnd.duckabary.core.library_management.Document;

import java.time.LocalDateTime;

public class BorrowCmdService extends BorrowService {

    public BorrowCmdService(BorrowManagement borrowManagement) {
        super(borrowManagement);
    }

    @Override
    public boolean borrowDocumentByUIdnDId(int userID, int documentID, int borrowQuantity, LocalDateTime dueDate) {

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
        if (document.getQuantity() <= 0||borrowQuantity>document.getQuantity()) {
            System.out.println("Document with id " + documentID + " is out of stock");
            return false;
        }

        BorrowRecord borrowRecord =
                BorrowRecord.createBorrowRecord(
                        borrowDatabaseManagement.getNextRecordId(),
                        userID,
                        documentID,
                        borrowQuantity,
                        LocalDateTime.now(),
                        dueDate);
        borrowManagement.saveBorrowRecord(borrowRecord);

        document.setQuantity(document.getQuantity() - 1);

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

        Document document = libraryService.getDocumentByID(docId);
        if (document == null) {
            System.out.println("Document with id " + docId + " not found");
            return false;
        }
        borrowManagement.returnDocument(record);

        document.setQuantity(document.getQuantity() + 1);

        System.out.println("Document returned successfully");
        return true;
    }
}
