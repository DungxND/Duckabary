package io.vn.dungxnd.duckabary;

import io.vn.dungxnd.duckabary.core.borrow_management.BorrowCmdService;
import io.vn.dungxnd.duckabary.core.borrow_management.BorrowManagement;
import io.vn.dungxnd.duckabary.core.borrow_management.BorrowRecord;
import io.vn.dungxnd.duckabary.core.library_management.Document;
import io.vn.dungxnd.duckabary.core.library_management.LibraryCmdService;
import io.vn.dungxnd.duckabary.core.library_management.LibraryManagement;
import io.vn.dungxnd.duckabary.core.user_management.User;
import io.vn.dungxnd.duckabary.core.user_management.UserCmdService;
import io.vn.dungxnd.duckabary.core.user_management.UserManagement;
import io.vn.dungxnd.duckabary.db.DatabaseManager;

import java.util.ArrayList;
import java.util.Scanner;

import static io.vn.dungxnd.duckabary.core.Utils.getFormattedTime;

public class AppCommandline {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        AppCliManagement appCliManagement = new AppCliManagement();

        UserManagement userManagement = appCliManagement.getUserManagement();
        UserCmdService userCmdServices = appCliManagement.getUserCmdService();

        LibraryManagement libraryManagement = appCliManagement.getLibraryManagement();
        LibraryCmdService libraryCmdService = appCliManagement.getLibraryCmdService();

        BorrowManagement borrowManagement = appCliManagement.getBorrowManagement();
        BorrowCmdService borrowService = appCliManagement.getBorrowCmdService();

        //noinspection InfiniteLoopStatement
        while (true) {
            System.out.println("====Library Management System====");
            System.out.println("0. Exit");
            System.out.println("1. Add document");
            System.out.println("2. Remove document");
            System.out.println("3. Update document");
            System.out.println("4. Find document");
            System.out.println("5. Display Document");
            System.out.println("6. Add User");
            System.out.println("7. Borrow Document");
            System.out.println("8. Return Document");
            System.out.println("9. Display User Info");
            System.out.println("10. Show user borrowed document(s)");
            System.out.print("Choose: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 0:
                    System.out.print("Exiting...");
                    scanner.close();
                    exitCommandline();
                    break;
                case 1:
                    addDocument(scanner, libraryCmdService);
                    break;
                case 2:
                    removeDocument(scanner, libraryCmdService);
                    break;
                case 3:
                    updateDocument(scanner, libraryCmdService);
                    break;
                case 4:
                    findDocument(scanner, libraryCmdService);
                    break;
                case 5:
                    displayDocumentList(libraryCmdService);
                    break;
                case 6:
                    addUser(scanner, userCmdServices);
                    break;
                case 7:
                    borrowDocument(scanner, libraryCmdService);
                    break;
                case 8:
                    returnDocument(scanner, libraryCmdService);
                    break;
                case 9:
                    displayUserInfo(scanner, userCmdServices);
                    break;
                case 10:
                    showUserBorrowedDocuments(scanner, libraryCmdService);
                    break;
                default:
                    System.out.println("Action not found");
            }
        }
    }

    static void showUserBorrowedDocuments(Scanner scanner, LibraryCmdService libraryCmdService) {
        System.out.println("====Show user borrowed documents=====");
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        UserCmdService userCmdServices = libraryCmdService.getUserService();
        User user = userCmdServices.getUser(userId);
        if (user != null) {
            System.out.println("User " + user.getUsername() + " borrowed documents: ");
            for (BorrowRecord r : user.getBorrowRecords()) {
                System.out.println("====Record ID: " + r.getRecordId()+"====");
                System.out.println("Document: " + libraryCmdService.getDocumentByID(r.getDocumentId()).getTitle() + " (ID: " + r.getDocumentId() + ")");
                System.out.println("Borrow date: " + getFormattedTime(r.getBorrowDate()));
                System.out.println("Due date: " + getFormattedTime(r.getDueDate()));
                if (r.getReturnDate() != null) {
                    System.out.println("Return date: " + getFormattedTime(r.getReturnDate()));
                }

            }
        } else {
            System.out.println("User not found");
        }
    }

    static void displayUserInfo(Scanner scanner, UserCmdService userCmdServices) {
        System.out.println("====Display user info=====");
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        userCmdServices.getUserInfo(userId);
    }

    static void returnDocument(Scanner scanner, LibraryCmdService LibraryCmdService) {
        System.out.println("====Return document=====");
        System.out.print("Enter user ID who return document: ");
        int userId = scanner.nextInt();
        System.out.print("Enter document ID to return: ");
        int returnId = scanner.nextInt();
        LibraryCmdService.returnDocumentByID(userId, returnId);
    }

    static void borrowDocument(Scanner scanner, LibraryCmdService LibraryCmdService) {
        System.out.println("====Borrow document=====");
        System.out.print("Enter user ID who borrow: ");
        int userId = scanner.nextInt();
        System.out.print("Enter document ID to borrow: ");
        int borrowId = scanner.nextInt();
        LibraryCmdService.borrowDocumentByID(userId, borrowId);
    }

    static void addUser(Scanner scanner, UserCmdService userService) {
        System.out.println("====Add user=====");
        System.out.print("Enter username: ");
        String userName = scanner.nextLine();
        System.out.print("Enter user email: ");
        String userId = scanner.nextLine();
        System.out.print("Enter user first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter user last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter user phone number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter user address: ");
        String address = scanner.nextLine();
        userService.createUser(userName, userId, firstName, lastName, phone, address);
    }

    static void displayDocumentList(LibraryCmdService LibraryCmdService) {
        System.out.println("====Display document=====");
        System.out.println("Document list: ");
        for (Document d : LibraryCmdService.getDocumentList()) {
            printDocumentInfo(d);
        }
        System.out.println();
    }

    static void findDocument(Scanner scanner, LibraryCmdService LibraryCmdService) {
        System.out.println("====Find document=====");
        System.out.println("Enter 0 to cancel");
        System.out.println("How do you want to find document?");
        System.out.println("1. Find by ID");
        System.out.println("2. Find by Name");
        System.out.println("3. Find by Author");
        System.out.println("4. Find by Genre");
        System.out.print("Enter searching option: ");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
            case 0:
                break;
            case 1:
                findDocumentByID(scanner, LibraryCmdService);
                break;
            case 2:
                findDocumentByTitle(scanner, LibraryCmdService);
                break;
            case 3:
                findDocumentByAuthor(scanner, LibraryCmdService);
                break;
            case 4:
                findDocumentByGenre(scanner, LibraryCmdService);
                break;
            default:
                System.out.println("Option not found");
        }
    }

    private static void findDocumentByID(Scanner scanner, LibraryCmdService LibraryCmdService) {
        int findId = scanner.nextInt();
        Document foundDocument = LibraryCmdService.getDocumentByID(findId);
        if (foundDocument != null) {
            printDocumentInfo(foundDocument);
        } else {
            System.out.println("Document not found");
        }
    }

    static void findDocumentByTitle(Scanner scanner, LibraryCmdService LibraryCmdService) {
        System.out.println("====Find document by title=====");
        System.out.println("Enter document title: ");
        String docName = scanner.nextLine();
        ArrayList<Document> foundDocuments = LibraryCmdService.getDocumentByTitle(docName);
        if (foundDocuments != null) {
            printDocumentInfoList(foundDocuments);
        }
    }

    static void findDocumentByAuthor(Scanner scanner, LibraryCmdService LibraryCmdService) {
        System.out.println("====Find document by Author=====");
        System.out.println("Enter document author: ");
        String docAuthor = scanner.nextLine();
        ArrayList<Document> foundDocuments = LibraryCmdService.getDocumentByAuthor(docAuthor);
        if (foundDocuments != null) {
            printDocumentInfoList(foundDocuments);
        }
    }

    static void findDocumentByGenre(Scanner scanner, LibraryCmdService LibraryCmdService) {
        System.out.println("====Find document by Genre=====");
        System.out.println("Enter document genre: ");
        String docGenre = scanner.nextLine();
        ArrayList<Document> foundDocuments = LibraryCmdService.getDocumentByGenre(docGenre);
        if (foundDocuments != null) {
            printDocumentInfoList(foundDocuments);
        }
    }

    static void updateDocument(Scanner scanner, LibraryCmdService libraryCmdService) {
        System.out.println("====Update document=====");
        System.out.print("Enter id: ");
        int getId = scanner.nextInt();
        Document document = libraryCmdService.getDocumentByID(getId);
        if (document != null) {
            scanner.nextLine();
            System.out.println("What do you want to update?");
            System.out.println("1. Title");
            System.out.println("2. Author");
            System.out.println("3. Description");
            System.out.println("4. Publisher");
            System.out.println("5. Publish year");
            System.out.println("6. Genre");
            System.out.println("7. Language");
            System.out.println("8. Quantity");
            System.out.println("9. ISBN");
            System.out.print("Choose: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.print("Enter new title: ");
                    String newTitle = scanner.nextLine();
                    document.setTitle(newTitle);
                    break;
                case 2:
                    System.out.print("Enter new author: ");
                    String newAuthor = scanner.nextLine();
                    document.setAuthor(newAuthor);
                    break;
                case 3:
                    System.out.print("Enter new description: ");
                    String newDesc = scanner.nextLine();
                    document.setDescription(new StringBuilder(newDesc));
                    break;
                case 4:
                    System.out.print("Enter new publisher: ");
                    String newPublisher = scanner.nextLine();
                    document.setPublisher(newPublisher);
                    break;
                case 5:
                    System.out.print("Enter new publish year: ");
                    int newPublishYear = scanner.nextInt();
                    document.setPublishYear(newPublishYear);
                    break;
                case 6:
                    System.out.print("Enter new genre: ");
                    String newGenre = scanner.nextLine();
                    document.setGenre(newGenre);
                    break;
                case 7:
                    System.out.print("Enter new language: ");
                    String newLanguage = scanner.nextLine();
                    document.setLanguage(newLanguage);
                    break;
                case 8:
                    System.out.print("Enter new quantity: ");
                    int newQuantity = scanner.nextInt();
                    document.setQuantity(newQuantity);
                    break;
                case 9:
                    System.out.print("Enter new ISBN: ");
                    String newISBN = scanner.nextLine();
                    document.setISBN(newISBN);
                    break;
                default:
                    System.out.println("Option not found");
            }
        } else {
            System.out.println("Document not found");
        }
    }

    static void removeDocument(Scanner scanner, LibraryCmdService libraryCmdService) {
        System.out.println("====Remove document=====");
        System.out.print("Enter id: ");
        int removeId = scanner.nextInt();
        libraryCmdService.removeDocumentByID(removeId);
    }

    static void addDocument(Scanner scanner, LibraryCmdService libraryCmdService) {
        System.out.println("====Add document====");
        System.out.println("Leave blank if unknown (except title)");
        String title = "";
        while (title.isBlank()) {
            System.out.print("Enter title: ");
            title = scanner.nextLine();
            if (title.isEmpty()) {
                System.out.println("Title is required");
            }
        }
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        if (author.isEmpty()) {
            author = "";
        }

        System.out.print("Enter description: ");
        String desc = scanner.nextLine();
        if (desc.isEmpty()) {
            desc = "";
        }
        System.out.print("Enter publisher: ");
        String publisher = scanner.nextLine();
        if (publisher.isEmpty()) {
            publisher = "";
        }
        System.out.print("Enter publish year: ");
        String publishYearInput = scanner.nextLine();
        int publishYear;
        try {
            publishYear = Integer.parseInt(publishYearInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Set correct publish year later, now set to 0");
            publishYear = 0;
        }
        System.out.print("Enter genre(s): ");
        String genre = scanner.nextLine();
        if (genre.isEmpty()) {
            genre = "";
        }
        System.out.print("Enter document language: ");
        String language = scanner.nextLine();
        if (language.isEmpty()) {
            language = "";
        }
        System.out.print("Enter ISBN:");
        String ISBN = scanner.nextLine();
        if (ISBN.isEmpty()) {
            ISBN = "";
        }
        System.out.print("Enter quantity: ");
        String quantityInput = scanner.nextLine();
        int quantity;
        try {
            quantity = Integer.parseInt(quantityInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Set correct quantity later, now set to 0");
            quantity = 0;
        }
        libraryCmdService.addDocument(
                title,
                author,
                new StringBuilder(desc),
                publisher,
                publishYear,
                genre,
                language,
                ISBN,
                quantity);
    }

    private static void printDocumentInfo(Document foundDocument) {
        System.out.println("====Document info=====");
        System.out.println("ID: " + foundDocument.getId());
        System.out.println("Title: " + foundDocument.getTitle());
        System.out.println("Author: " + foundDocument.getAuthor());
        System.out.println("Desc: " + foundDocument.getDescription());
        System.out.println("Publisher: " + foundDocument.getPublisher());
        System.out.println("Publication year: " + foundDocument.getPublishYear());
        System.out.println("Genre: " + foundDocument.getGenre());
        System.out.println("Language: " + foundDocument.getLanguage());
        System.out.println("ISBN: " + foundDocument.getISBN());
        System.out.println("Quantity: " + foundDocument.getQuantity());
        System.out.println("==================================");
    }

    private static void printDocumentInfoList(ArrayList<Document> foundDocuments) {
        for (Document d : foundDocuments) {
            printDocumentInfo(d);
        }
    }

    private static void exitCommandline() {
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseManager::close));
        System.exit(0);
    }
}