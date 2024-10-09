package io.vn.dungxnd.duckabary.core.library_management;

import io.vn.dungxnd.duckabary.core.user_management.UserManagement;
import io.vn.dungxnd.duckabary.db.DatabaseManager;

import java.util.Scanner;


public class AppCommandline {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LibraryManagement libraryManagement = new LibraryManagement();
        Library library = libraryManagement.getLibrary();

        UserManagement userManagement = new UserManagement();

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
                    addDocument(scanner, library);
                    break;
                case 2:
                    removeDocument(scanner, library);
                    break;
                case 3:
                    updateDocument(scanner, library);
                    break;
                case 4:
                    findDocument(scanner, library);
                    break;
                case 5:
                    displayDocumentList(library);
                    break;
                case 6:
                    addUser(scanner, userManagement);
                    break;
                case 7:
                    borrowDocument(scanner, libraryManagement);
                    break;
                case 8:
                    returnDocument(scanner, libraryManagement);
                    break;
                case 9:
                    displayUserInfo(scanner, userManagement);
                    break;
                default:
                    System.out.println("Action not found");
            }
        }
    }

    static void displayUserInfo(Scanner scanner, UserManagement userManagement) {
        System.out.println("====Display user info=====");
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        userManagement.getUserInfo(userId);
    }

    static void returnDocument(Scanner scanner, LibraryManagement libraryManagement) {
        System.out.println("====Return document=====");
        System.out.print("Enter document ID to return: ");
        int returnId = scanner.nextInt();
        libraryManagement.returnDocumentByID(returnId);
    }

    static void borrowDocument(Scanner scanner, LibraryManagement libraryManagement) {
        System.out.println("====Borrow document=====");
        System.out.print("Enter document ID to borrow: ");
        int borrowId = scanner.nextInt();
        libraryManagement.borrowDocumentByID(borrowId);
    }

    static void addUser(Scanner scanner, UserManagement userManagement) {
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
        userManagement.createUser(userName, userId, firstName, lastName, phone, address);
    }

    static void displayDocumentList(Library library) {
        System.out.println("====Display document=====");
        System.out.println("Document list: ");
        for (Document d : library.getDocumentList()) {
            System.out.println("Title: " + d.getTitle());
            System.out.println("Author: " + d.getAuthor());
            System.out.println("Desc: " + d.getDescription());
        }
    }

    static void findDocument(Scanner scanner, Library library) {
        System.out.println("====Find document=====");
        System.out.println("Enter searching option: ");
        System.out.print("Enter id: ");
        int findId = scanner.nextInt();
        Document findDocument = library.getDocument(findId);
        if (findDocument != null) {
            System.out.println("Title: " + findDocument.getTitle());
            System.out.println("Author: " + findDocument.getAuthor());
            System.out.println("Desc: " + findDocument.getDescription());
        } else {
            System.out.println("Document not found");
        }
    }

    static void updateDocument(Scanner scanner, Library library) {
        System.out.println("====Update document=====");
        System.out.print("Enter id: ");
        int getId = scanner.nextInt();
        Document document = library.getDocument(getId);
        if (document != null) {
            System.out.println("Title: " + document.getTitle());
            System.out.println("Author: " + document.getAuthor());
            System.out.println("Desc: " + document.getDescription());
        } else {
            System.out.println("Document not found");
        }
    }

    static void removeDocument(Scanner scanner, Library library) {
        System.out.println("====Remove document=====");
        System.out.print("Enter id: ");
        int removeId = scanner.nextInt();
        library.removeDocument(removeId);
    }

    static void addDocument(Scanner scanner, Library library) {
        System.out.println("====Add document====");
        System.out.print("Enter id: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter desc: ");
        String desc = scanner.nextLine();
        Document doc = new Document(id, title, author, publisher, new StringBuilder(desc), 1);
        library.addDocument(doc);
    }

    private static void exitCommandline() {
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseManager::close));
        System.exit(0);
    }

}
