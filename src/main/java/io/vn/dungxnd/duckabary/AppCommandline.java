package io.vn.dungxnd.duckabary;

import io.vn.dungxnd.duckabary.core.library_management.Document;
import io.vn.dungxnd.duckabary.core.library_management.LibraryCmdService;
import io.vn.dungxnd.duckabary.core.library_management.LibraryManagement;
import io.vn.dungxnd.duckabary.core.user_management.UserCmdService;
import io.vn.dungxnd.duckabary.core.user_management.UserManagement;
import io.vn.dungxnd.duckabary.core.user_management.UserService;
import io.vn.dungxnd.duckabary.db.DatabaseManager;

import java.util.ArrayList;
import java.util.Scanner;

public class AppCommandline {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        LibraryManagement libraryManagement = new LibraryManagement();
        LibraryCmdService libraryCmdService = new LibraryCmdService(libraryManagement);

        UserManagement userManagement = new UserManagement();
        UserCmdService userCmdServices = new UserCmdService(userManagement);

        while (true) {
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
            System.out.print("Choose: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 0:
                    System.out.print("Exiting...");
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
                default:
                    System.out.println("Action not found");
            }
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
        System.out.print("Enter document ID to return: ");
        int returnId = scanner.nextInt();
        LibraryCmdService.returnDocumentByID(returnId);
    }

    static void borrowDocument(Scanner scanner, LibraryCmdService LibraryCmdService) {
        System.out.println("====Borrow document=====");
        System.out.print("Enter document ID to borrow: ");
        int borrowId = scanner.nextInt();
        LibraryCmdService.borrowDocumentByID(borrowId);
    }

    static void addUser(Scanner scanner, UserService userService) {
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
        System.out.println("1. Find by ID");
        System.out.println("2. Find by Name");
        System.out.println("3. Find by Author");
        System.out.println("Enter searching option: ");
        int option = scanner.nextInt();
        switch (option) {
            case 1:
                findDocumentByID(scanner, LibraryCmdService);
                break;
            case 2:
                findDocumentByTitle(scanner, LibraryCmdService);
                break;
            case 3:
                findDocumentByAuthor(scanner, LibraryCmdService);
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
        scanner.nextLine();
        System.out.print("Leave blank if unknown (except title)");
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        if (title.isEmpty()) {
            System.out.println("Title is required");
            return;
        }
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter description: ");
        String desc = scanner.nextLine();
        System.out.print("Enter publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter publish year: ");
        int publishYear = scanner.nextInt();
        System.out.print("Enter genre(s): ");
        String genre = scanner.nextLine();
        System.out.print("Enter document language: ");
        String language = scanner.nextLine();
        System.out.print("Enter ISBN:");
        String ISBN = scanner.nextLine();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        libraryCmdService.addDocument(title, author, new StringBuilder(desc), publisher, publishYear, genre, language, ISBN, quantity);
    }

    private static void printDocumentInfo(Document foundDocument) {
        System.out.println("====Document info=====");
        System.out.println("ID: " + foundDocument.getId());
        System.out.println("Title: " + foundDocument.getTitle());
        System.out.println("Author: " + foundDocument.getAuthor());
        System.out.println("Desc: " + foundDocument.getDescription());
        System.out.println("Publisher: " + foundDocument.getPublisher());
        System.out.println("Publication year: " + foundDocument.getPublishYear());
        System.out.println("Quantity: " + foundDocument.getQuantity());
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
