//package io.vn.dungxnd.duckabary.core.library_management;
//
//import io.vn.dungxnd.duckabary.core.user_management.User;
//import io.vn.dungxnd.duckabary.core.user_management.UserManagement;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.PrintStream;
//import java.util.Scanner;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//class AppCommandlineTest {
//
//    private LibraryManagement libraryManagement;
//    private UserManagement userManagement;
//    private ByteArrayOutputStream outContent;
//
//    @BeforeEach
//    void setUp() {
//        libraryManagement = new LibraryManagement();
//        userManagement = new UserManagement();
//        outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//    }
//
//    @Test
//    void testAddDocument() {
//        String input = "1\nTest Title\nTest Author\nTest Publisher\nTest Description\n";
//        System.setIn(new ByteArrayInputStream(input.getBytes()));
//        Scanner scanner = new Scanner(System.in);
//
//        AppCommandline.addDocument(scanner, libraryManagement.getLibrary());
//
//        assertEquals(1, libraryManagement.getLibrary().getDocumentList().size());
//        Document doc = libraryManagement.getLibrary().getDocumentList().get(0);
//        assertEquals("Test Title", doc.getTitle());
//        assertEquals("Test Author", doc.getAuthor());
//        assertEquals("Test Publisher", doc.getPublisher());
//        assertEquals("Test Description", doc.getDescription().toString());
//    }
//
//    @Test
//    void testRemoveDocument() {
//        Document doc = new Document(1, "Title", "Author");
//        libraryManagement.getLibrary().addDocument(doc);
//
//        String input = "1\n";
//        System.setIn(new ByteArrayInputStream(input.getBytes()));
//        Scanner scanner = new Scanner(System.in);
//
//        AppCommandline.removeDocument(scanner, libraryManagement.getLibrary());
//
//        assertEquals(0, libraryManagement.getLibrary().getDocumentList().size());
//    }
//
//    @Test
//    void testBorrowDocument() {
//        Document doc = new Document(1, "Title", "Author");
//        doc.setQuantity(1);
//        libraryManagement.getLibrary().addDocument(doc);
//
//        String input = "1\n";
//        System.setIn(new ByteArrayInputStream(input.getBytes()));
//        Scanner scanner = new Scanner(System.in);
//
//        AppCommandline.borrowDocument(scanner, libraryManagement);
//
//        assertEquals(0, doc.getQuantity());
//        assertTrue(outContent.toString().contains("Document borrowed successfully"));
//    }
//
//    @Test
//    void testReturnDocument() {
//        Document doc = new Document(1, "Title", "Author");
//        doc.setQuantity(0);
//        libraryManagement.getLibrary().addDocument(doc);
//
//        String input = "1\n";
//        System.setIn(new ByteArrayInputStream(input.getBytes()));
//        Scanner scanner = new Scanner(System.in);
//
//        AppCommandline.returnDocument(scanner, libraryManagement);
//
//        assertEquals(1, doc.getQuantity());
//        assertTrue(outContent.toString().contains("Document returned successfully"));
//    }
//
//    @Test
//    void testAddUser() {
//        String input = "TestUser\nTestEmail\n";
//        System.setIn(new ByteArrayInputStream(input.getBytes()));
//        Scanner scanner = new Scanner(System.in);
//
//        AppCommandline.addUser(scanner, userManagement);
//
//        assertEquals(1, userManagement.getUsers().size());
//        assertEquals("TestUser", userManagement.getUsers().getFirst().getUsername());
//        assertEquals("TestEmail", userManagement.getUsers().getFirst().getEmail());
//    }
//
//    @Test
//    void testDisplayUserInfo() {
//        User user = new User("TestUser", "TestEmail");
//        userManagement.createUser(user.getUsername(), user.getEmail());
//
//        String input = "0\n";
//        System.setIn(new ByteArrayInputStream(input.getBytes()));
//        Scanner scanner = new Scanner(System.in);
//
//        AppCommandline.displayUserInfo(scanner, userManagement);
//
//        assertTrue(outContent.toString().contains("Username: TestUser"));
//        assertTrue(outContent.toString().contains("Email: TestEmail"));
//    }
//}