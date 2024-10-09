package io.vn.dungxnd.duckabary.core.library_management;

import io.vn.dungxnd.duckabary.core.user_management.UserManagement;
import io.vn.dungxnd.duckabary.core.user_management.UserServices;
import io.vn.dungxnd.duckabary.db.DatabaseManager;

import java.util.ArrayList;
import java.util.Scanner;

public class AppCommandline {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        LibraryManagement libraryManagement = new LibraryManagement();
        Library library = libraryManagement.getLibrary();
        LibraryServices libraryServices = new LibraryServices(libraryManagement);

        UserManagement userManagement = new UserManagement();
        UserServices userServices = new UserServices(userManagement);

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
                    addUser(scanner, userServices);
                    break;
                case 7:
                    borrowDocument(scanner, libraryServices);
                    break;
                case 8:
                    returnDocument(scanner, libraryServices);
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

    static void returnDocument(Scanner scanner, LibraryServices libraryServices) {
        System.out.println("====Return document=====");
        System.out.print("Enter document ID to return: ");
        int returnId = scanner.nextInt();
        libraryServices.returnDocumentByID(returnId);
    }

    static void borrowDocument(Scanner scanner, LibraryServices libraryServices) {
        System.out.println("====Borrow document=====");
        System.out.print("Enter document ID to borrow: ");
        int borrowId = scanner.nextInt();
        libraryServices.borrowDocumentByID(borrowId);
    }

    static void addUser(Scanner scanner, UserServices userServices) {
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
        userServices.createUser(userName, userId, firstName, lastName, phone, address);
    }

    static void displayDocumentList(Library library) {
        System.out.println("====Display document=====");
        System.out.println("Document list: ");
        for (Document d : library.getDocumentList()) {
            printDocumentInfo(d);
        }
        System.out.println();
    }

    static void findDocument(Scanner scanner, Library library) {
        System.out.println("====Find document=====");
        System.out.println("1. Find by ID");
        System.out.println("2. Find by Name");
        System.out.println("3. Find by Author");
        System.out.println("Enter searching option: ");
        int option = scanner.nextInt();
        switch (option) {
            case 1:
                findDocumentByID(scanner, library);
                break;
            case 2:
                findDocumentByName(scanner, library);
                break;
            case 3:
                findDocumentByAuthor(scanner, library);
                break;
            default:
                System.out.println("Option not found");
        }
    }

    private static void findDocumentByID(Scanner scanner, Library library) {
        int findId = scanner.nextInt();
        Document foundDocument = library.getDocument(findId);
        if (foundDocument != null) {
            printDocumentInfo(foundDocument);
        } else {
            System.out.println("Document not found");
        }
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

    static void findDocumentByName(Scanner scanner, Library library) {
        System.out.println("====Find document by Name=====");
        System.out.println("Enter document name: ");
        String docName = scanner.nextLine();
        ArrayList<Document> foundDocuments = library.getDocumentByName(docName);
        if (foundDocuments != null) {
        } else {
            System.out.println("Document not found!");
        }
    }

    static void findDocumentByAuthor(Scanner scanner, Library library) {
        System.out.println("====Find document by Author=====");
        System.out.println("Enter document author: ");
        String docAuthor = scanner.nextLine();
        ArrayList<Document> foundDocuments = library.getDocumentByAuthor(docAuthor);
        if (foundDocuments != null) {
            printDocumentInfoList(foundDocuments);
        } else {
            System.out.println("No document found!");
        }
    }

    static void updateDocument(Scanner scanner, Library library) {
        System.out.println("====Update document=====");
        System.out.print("Enter id: ");
        int getId = scanner.nextInt();
        Document document = library.getDocument(getId);
        if (document != null) {
            scanner.nextLine();
            System.out.print("Enter new title: ");
            String newTitle = scanner.nextLine();
            System.out.print("Enter new author: ");
            String newAuthor = scanner.nextLine();
            System.out.print("Enter new description: ");
            String newdes = scanner.nextLine();
            StringBuilder newDescription = new StringBuilder(newdes);
            document.setTitle(newTitle);
            document.setAuthor(newAuthor);
            document.setDescription(newDescription);
            printDocumentInfo(document);
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
