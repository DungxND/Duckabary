package io.vn.dungxnd.duckabary.presentation.cli;

import static io.vn.dungxnd.duckabary.util.PasswordUtils.hashPassword;
import static io.vn.dungxnd.duckabary.util.TimeUtils.*;

import io.vn.dungxnd.duckabary.domain.database.DatabaseManager;
import io.vn.dungxnd.duckabary.domain.model.library.*;
import io.vn.dungxnd.duckabary.domain.model.user.Manager;
import io.vn.dungxnd.duckabary.domain.model.user.User;
import io.vn.dungxnd.duckabary.domain.service.borrow.BorrowService;
import io.vn.dungxnd.duckabary.domain.service.library.DocumentService;
import io.vn.dungxnd.duckabary.domain.service.user.ManagerService;
import io.vn.dungxnd.duckabary.domain.service.user.UserService;
import io.vn.dungxnd.duckabary.exeption.DatabaseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AppCommandline {
    private static final Logger log = LoggerFactory.getLogger(AppCommandline.class);
    private final DocumentService documentService;
    private final UserService userService;
    private final BorrowService borrowService;
    private final ManagerService managerService;

    private Manager currentManager = null;

    public AppCommandline(AppCliManagement management) {
        this.documentService = management.getDocumentService();
        this.userService = management.getUserService();
        this.borrowService = management.getBorrowService();
        this.managerService = management.getManagerService();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AppCliManagement management = new AppCliManagement();
        AppCommandline cli = new AppCommandline(management);

        while (true) {
            if (cli.currentManager == null) {
                System.out.println("====Library Management System====");
                System.out.println("1. Login");
                System.out.println("2. Register new manager");
                System.out.println("0. Exit");

                try {
                    int choice = readInteger(scanner, "Choose: ");
                    scanner.nextLine();

                    switch (choice) {
                        case 0 -> {
                            System.out.print("Exiting...");
                            scanner.close();
                            exitCommandline();
                        }
                        case 1 -> cli.login(scanner);
                        case 2 -> cli.registerManager(scanner);
                        default -> System.out.println("Invalid choice");
                    }
                } catch (Exception e) {
                    System.out.println("Error when login/register manager: " + e.getMessage());
                }
            } else {
                cli.showMainMenu(scanner);
            }
        }
    }

    private static void exitCommandline() {
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseManager::close));
        System.exit(0);
    }

    private static int readInteger(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine();
            }
        }
    }

    private static long readLong(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextLong()) {
                return scanner.nextLong();
            } else {
                System.out.println("Invalid input. Please enter a valid long.");
                scanner.nextLine();
            }
        }
    }

    private static String readString(Scanner scanner, String prompt, boolean required) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!required || !input.isEmpty()) {
                return input;
            }
            System.out.println("This field is required.");
        }
    }

    private void login(Scanner scanner) {
        System.out.println("====Manager Login====");
        String username = readString(scanner, "Enter username: ", true);
        String password = readString(scanner, "Enter password: ", true);

        try {
            if (managerService.validateCredentials(username, password)) {
                currentManager = managerService.findByUsername(username).orElseThrow();
                System.out.println("Login successful. Welcome, " + currentManager.username());
            } else {
                System.out.println("Invalid credentials");
            }
        } catch (DatabaseException e) {
            System.out.println("Login error: " + e.getMessage());
        }
    }

    private void registerManager(Scanner scanner) {
        System.out.println("====Register New Manager====");
        String username = readString(scanner, "Enter username: ", true);
        String email = readString(scanner, "Enter email: ", true);
        String rawPassword = readString(scanner, "Enter password: ", true);

        try {
            if (!managerService.isUsernameAvailable(username)
                    || !userService.isUsernameAvailable(username)) {
                System.out.println("Username already taken");
                return;
            }

            if (rawPassword.length() < 8) {
                System.out.println("Password must be at least 8 characters long");
                return;
            }

            Manager newManager =
                    Manager.createManager(0, username, email, hashPassword(rawPassword));
            newManager = managerService.saveManager(newManager);

            System.out.println(
                    "Manager"
                            + username
                            + " registered successfully with ID: "
                            + newManager.managerId());

        } catch (DatabaseException e) {
            System.out.println("Registration error: " + e.getMessage());
        }
    }

    private void logout() {
        currentManager = null;
        System.out.println("Logged out successfully");
        exitCommandline();
    }

    private void showMainMenu(Scanner scanner) {
        System.out.println("====Library Management System====");
        System.out.println("0. Logout & exit");
        System.out.println("1. Add document");
        System.out.println("2. Remove document");
        System.out.println("3. Update document");
        System.out.println("4. Find document");
        System.out.println("5. Add new user");
        System.out.println("6. Borrow document");
        System.out.println("7. Return document");
        System.out.println("8. Show user Info");
        System.out.println("9. Show user borrowed document(s)");
        System.out.println("10. Show document info");

        try {
            int choice = readInteger(scanner, "Choose: ");
            scanner.nextLine();
            switch (choice) {
                case 0 -> {
                    System.out.print("Exiting...");
                    scanner.close();
                    logout();
                }
                case 1 -> addDocument(scanner);
                case 2 -> removeDocument(scanner);
                case 3 -> updateDocument(scanner);
                case 4 -> findDocument(scanner);
                case 5 -> addUser(scanner);
                case 6 -> borrowDocument(scanner);
                case 7 -> returnDocument(scanner);
                case 8 -> displayUserInfo(scanner);
                case 9 -> showUserBorrowedDocuments(scanner);
                case 10 -> showDocumentInfo(scanner);
                default -> System.out.println("Action not found");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        } catch (DatabaseException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private void addDocument(Scanner scanner) {
        System.out.println("====Add document=====");
        System.out.println("Enter document details, type and title are required.");

        System.out.println("Select document type:");
        System.out.println("1. Book");
        System.out.println("2. Journal");
        System.out.println("3. Thesis");

        int typeChoice = readInteger(scanner, "Choose document type: ");
        scanner.nextLine();

        if (typeChoice < 1 || typeChoice > 3) {
            System.out.println("Invalid document type");
            return;
        }

        String title = readString(scanner, "Enter title: ", true);

        String author = readString(scanner, "Enter author: ", false);

        String description = readString(scanner, "Enter description: ", false);

        int publishYear;
        String publishYearStr = readString(scanner, "Enter publish year: ", false);
        if (publishYearStr.isEmpty() || Integer.parseInt(publishYearStr) <= 0) {
            System.out.println("Invalid publish year, set to 0");
            publishYear = 0;
        } else {
            publishYear = Integer.parseInt(publishYearStr);
        }

        int quantity = readInteger(scanner, "Enter quantity: ");
        scanner.nextLine();
        if (quantity < 0) {
            System.out.println("Quantity must not be negative");
            return;
        }

        Document document =
                switch (typeChoice) {
                    case 1 -> {
                        String isbn = readString(scanner, "Enter ISBN: ", false);
                        String publisher = readString(scanner, "Enter publisher: ", false);
                        String language =
                                readString(
                                        scanner, "Enter language (ISO-639 Set 1 Format): ", false);
                        String genre = readString(scanner, "Enter genre: ", false);
                        yield new Book(
                                null,
                                title,
                                author,
                                description,
                                publishYear,
                                quantity,
                                "BOOK",
                                isbn,
                                publisher,
                                language,
                                genre);
                    }
                    case 2 -> {
                        String issn = readString(scanner, "Enter ISSN: ", false);
                        String volume = readString(scanner, "Enter volume: ", false);
                        String issue = readString(scanner, "Enter issue: ", false);
                        yield new Journal(
                                null,
                                title,
                                author,
                                description,
                                publishYear,
                                quantity,
                                "JOURNAL",
                                issn,
                                volume,
                                issue);
                    }
                    case 3 -> {
                        String university = readString(scanner, "Enter university: ", false);
                        String department = readString(scanner, "Enter department: ", false);
                        String supervisor = readString(scanner, "Enter supervisor: ", false);
                        String degree = readString(scanner, "Enter degree: ", false);
                        String defenseDateStr =
                                readString(
                                        scanner, "Enter defense date (yyyy-MM-dd HH:mm): ", false);

                        Optional<LocalDateTime> defenseDate =
                                defenseDateStr.isEmpty()
                                        ? Optional.empty()
                                        : Optional.of(getDateTimeFromString(defenseDateStr));
                        yield new Thesis(
                                null,
                                title,
                                author,
                                description,
                                publishYear,
                                quantity,
                                "THESIS",
                                university,
                                department,
                                supervisor,
                                degree,
                                defenseDate);
                    }
                    default -> throw new IllegalArgumentException("Invalid document type");
                };

        try {
            documentService.saveDocument(document);
            System.out.println(
                    "Document "
                            + title
                            + "(type "
                            + document.type().toLowerCase()
                            + ") added successfully with ID: "
                            + document.id());
        } catch (DatabaseException e) {
            System.out.println("Error adding document: " + e.getMessage());
        }
    }

    private void removeDocument(Scanner scanner) {
        System.out.println("====Remove document=====");
        long docId = readInteger(scanner, "Enter document ID: ");
        scanner.nextLine();
        try {
            documentService.deleteDocument(docId);
            System.out.println("Document removed successfully");
        } catch (DatabaseException e) {
            System.out.println("Error removing document: " + e.getMessage());
        }
    }

    private void updateDocument(Scanner scanner) {
        System.out.println("====Update document=====");
        long docId = readInteger(scanner, "Enter document ID: ");
        scanner.nextLine();
        try {
            Document document = documentService.getDocumentById(docId);
            printDocumentInfo(document);
            System.out.println("Enter new information for the document");
            System.out.println("Enter document details, type and title are required.");

            System.out.println("Select document type:");
            System.out.println("1. Book");
            System.out.println("2. Journal");
            System.out.println("3. Thesis");

            int typeChoice = readInteger(scanner, "Choose document type: ");
            scanner.nextLine();
            if (typeChoice < 1 || typeChoice > 3) {
                System.out.println("Invalid document type");
                return;
            }

            String title = readString(scanner, "Enter title: ", true);

            String author = readString(scanner, "Enter author: ", false);

            String description = readString(scanner, "Enter description: ", false);

            int publishYear;
            String publishYearStr = readString(scanner, "Enter publish year: ", false);
            if (publishYearStr.isEmpty() || Integer.parseInt(publishYearStr) <= 0) {
                System.out.println("Invalid publish year, set to 0");
                publishYear = 0;
            } else {
                publishYear = Integer.parseInt(publishYearStr);
            }

            int quantity = readInteger(scanner, "Enter quantity: ");
            scanner.nextLine();
            if (quantity < 0) {
                System.out.println("Quantity must not be negative");
                return;
            }
            Document newDocument =
                    switch (typeChoice) {
                        case 1 -> {
                            String isbn = readString(scanner, "Enter ISBN: ", false);
                            String publisher = readString(scanner, "Enter publisher: ", false);
                            String language =
                                    readString(scanner, "Enter language (ISO-639): ", false);
                            String genre = readString(scanner, "Enter genre: ", false);
                            yield new Book(
                                    docId,
                                    title,
                                    author,
                                    description,
                                    publishYear,
                                    quantity,
                                    "BOOK",
                                    isbn,
                                    publisher,
                                    language,
                                    genre);
                        }
                        case 2 -> {
                            String issn = readString(scanner, "Enter ISSN: ", false);
                            String volume = readString(scanner, "Enter volume: ", false);
                            String issue = readString(scanner, "Enter issue: ", false);
                            yield new Journal(
                                    docId,
                                    title,
                                    author,
                                    description,
                                    publishYear,
                                    quantity,
                                    "JOURNAL",
                                    issn,
                                    volume,
                                    issue);
                        }
                        case 3 -> {
                            String university = readString(scanner, "Enter university: ", false);
                            String department = readString(scanner, "Enter department: ", false);
                            String supervisor = readString(scanner, "Enter supervisor: ", false);
                            String degree = readString(scanner, "Enter degree: ", false);
                            yield new Thesis(
                                    docId,
                                    title,
                                    author,
                                    description,
                                    publishYear,
                                    quantity,
                                    "THESIS",
                                    university,
                                    department,
                                    supervisor,
                                    degree,
                                    Optional.empty());
                        }
                        default -> throw new IllegalArgumentException("Invalid document type");
                    };
            documentService.saveDocument(newDocument);
            System.out.println("Document with ID " + docId + " updated successfully");
        } catch (DatabaseException e) {
            System.out.println("Error updating document: " + e.getMessage());
        }
    }

    private void findDocument(Scanner scanner) {
        System.out.println("====Find document=====");
        System.out.println("1. Search by title");
        System.out.println("2. Search by author");
        System.out.println("3. Search by type");
        System.out.println("4. Get document count by type");

        int choice = readInteger(scanner, "Choose search option: ");
        scanner.nextLine();
        switch (choice) {
            case 1 -> {
                String title = readString(scanner, "Enter title: ", true);
                List<Document> documents = documentService.searchByTitle(title);
                printDocumentListInfo(documents);
            }
            case 2 -> {
                String author = readString(scanner, "Enter author: ", true);
                List<Document> documents = documentService.searchByAuthor(author);
                printDocumentListInfo(documents);
            }
            case 3 -> {
                String type = readString(scanner, "Enter type: ", true);
                List<Document> documents = documentService.getDocumentsByType(type);
                printDocumentListInfo(documents);
            }
            case 4 -> {
                String type = readString(scanner, "Enter type: ", true);
                int count = documentService.getDocumentCountByType(type);
                System.out.println("Total documents of type " + type + ": " + count);
            }
            default -> System.out.println("Invalid search option");
        }
    }

    private void addUser(Scanner scanner) {
        System.out.println("====Add new user=====");
        String username = readString(scanner, "Enter username: ", true);
        String firstName = readString(scanner, "Enter first name: ", true);
        String lastName = readString(scanner, "Enter last name: ", true);
        String email = readString(scanner, "Enter email: ", true);
        String phone = readString(scanner, "Enter phone number: ", true);
        String address = readString(scanner, "Enter address: ", true);
        try {
            User newUser = User.createUser(0, username, firstName, lastName, email, phone, address);
            userService.saveUser(newUser);
            System.out.println("User added successfully");
        } catch (IllegalArgumentException e) {
            System.out.println("Error input: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    private void borrowDocument(Scanner scanner) {
        System.out.println("====Borrow document=====");
        int userId = readInteger(scanner, "Enter user ID: ");
        scanner.nextLine();
        Long docId = readLong(scanner, "Enter document ID: ");
        scanner.nextLine();
        int quantity = readInteger(scanner, "Enter quantity: ");
        scanner.nextLine();
        String dueDateStr = readString(scanner, "Enter due date (yyyy-MM-dd HH:mm): ", true);
        if (isValidDateTime(dueDateStr)) {
            System.out.println("Invalid date format");
            return;
        }
        if (borrowService.isDocumentAvailableForBorrow(docId, quantity)) {
            System.out.println("Document not available in requested quantity");
            return;
        }
        try {
            BorrowRecord record =
                    borrowService.borrowDocument(
                            userId, docId, quantity, getDateTimeFromString(dueDateStr));
            System.out.println(
                    "Document with title "
                            + documentService.getDocumentById(docId).title()
                            + " borrowed successfully "
                            + quantity
                            + " copies by user "
                            + userService.getUserById(userId).username()
                            + " with record ID: "
                            + record.recordId());
        } catch (DatabaseException e) {
            System.out.println("Error borrowing document: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error input: " + e.getMessage());
        }
    }

    private void returnDocument(Scanner scanner) {
        System.out.println("====Return document=====");
        try {
            int borrowId = readInteger(scanner, "Enter borrow record ID to return: ");
            scanner.nextLine();

            BorrowRecord returnedRecord = borrowService.returnDocument(borrowId);
            System.out.println(
                    "Document with title "
                            + documentService.getDocumentById(returnedRecord.documentId()).title()
                            + " returned successfully "
                            + returnedRecord.quantity()
                            + " copies by user "
                            + userService.getUserById(returnedRecord.userId()).username());
            System.out.println(
                    "Return date: " + getFormattedTime(returnedRecord.returnDate().get()));
        } catch (DatabaseException e) {
            System.out.println("Error returning document: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    private void displayUserInfo(Scanner scanner) {
        System.out.println("====Display user info=====");
        try {
            int userId = readInteger(scanner, "Enter user ID: ");
            scanner.nextLine();

            User user = userService.getUserById(userId);
            System.out.println("====User Info=====");
            System.out.println("ID: " + user.id());
            System.out.println("Username: " + user.username());
            System.out.println("Name: " + user.firstName() + " " + user.lastName());
            System.out.println("Email: " + user.email());
            System.out.println("Phone: " + user.phone());
            System.out.println("Address: " + user.address());

            List<BorrowRecord> activeRecords =
                    borrowService.getBorrowsByUser(userId).stream()
                            .filter(record -> !record.isReturned())
                            .toList();

            if (!activeRecords.isEmpty()) {
                System.out.println("\nActive borrows:");
                for (BorrowRecord record : activeRecords) {
                    Document doc = documentService.getDocumentById(record.documentId());
                    System.out.println(
                            "- "
                                    + doc.title()
                                    + " (Due: "
                                    + getFormattedTime(record.dueDate())
                                    + ")");
                }
            }

        } catch (DatabaseException e) {
            System.out.println("Error retrieving user info: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    private void showDocumentInfo(Scanner scanner) {
        System.out.println("====Show document info=====");
        long docId = readInteger(scanner, "Enter document ID: ");
        scanner.nextLine();

        try {
            Document document = documentService.getDocumentById(docId);
            printDocumentInfo(document);
        } catch (DatabaseException e) {
            System.out.println("Document not found");
        }
    }

    private void printDocumentInfo(Document document) {
        System.out.println("====Document Info====");
        System.out.println("ID: " + document.id());
        System.out.println("Title: " + document.title());
        System.out.println("Author: " + document.author());
        System.out.println("Description: " + document.description());
        System.out.println("Publish year: " + document.publishYear());
        System.out.println("Quantity: " + document.quantity());
        System.out.println("Type: " + document.type());
        switch (document) {
            case Book book -> {
                System.out.println("ISBN: " + book.isbn());
                System.out.println("Publisher: " + book.publisher());
                System.out.println("Language: " + book.language());
                System.out.println("Genre: " + book.genre());
            }
            case Journal journal -> {
                System.out.println("ISSN: " + journal.issn());
                System.out.println("Volume: " + journal.volume());
                System.out.println("Issue: " + journal.issue());
            }
            case Thesis thesis -> {
                System.out.println("University: " + thesis.university());
                System.out.println("Department: " + thesis.department());
                System.out.println("Supervisor: " + thesis.supervisor());
                System.out.println("Degree: " + thesis.degree());
                thesis.defenseDate()
                        .ifPresentOrElse(
                                date ->
                                        System.out.println(
                                                "Defense date: " + getFormattedTime(date)),
                                () -> System.out.println("Defense date: Not set"));
            }
            default -> {}
        }
    }

    private void printDocumentListInfo(List<Document> documents) {
        System.out.println("====Document List Info====");
        for (Document document : documents) {
            printDocumentInfo(document);
        }
    }

    private void showUserBorrowedDocuments(Scanner scanner) {
        System.out.println("====Show user borrowed documents=====");
        int userId = readInteger(scanner, "Enter user ID: ");
        scanner.nextLine();
        List<BorrowRecord> borrowRecords = borrowService.getBorrowsByUser(userId);

        if (borrowRecords.isEmpty()) {
            System.out.println("User has not borrowed any document yet.");
            return;
        }

        for (BorrowRecord record : borrowRecords) {
            System.out.println("====Record ID: " + record.recordId() + "====");
            try {
                Document doc = documentService.getDocumentById(record.documentId());
                System.out.println("Document: " + doc.title() + " (ID: " + doc.id() + ")");
                System.out.println("Borrow date: " + getFormattedTime(record.borrowDate()));
                System.out.println("Due date: " + getFormattedTime(record.dueDate()));
                record.returnDate()
                        .ifPresentOrElse(
                                returnDate ->
                                        System.out.println(
                                                "Return date: " + getFormattedTime(returnDate)),
                                () -> System.out.println("Document not returned"));
            } catch (DatabaseException e) {
                System.out.println("Error retrieving document information");
            }
        }
    }
}
