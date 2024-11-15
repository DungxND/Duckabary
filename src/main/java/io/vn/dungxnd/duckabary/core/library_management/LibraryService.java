package io.vn.dungxnd.duckabary.core.library_management;

import io.vn.dungxnd.duckabary.core.user_management.User;
import io.vn.dungxnd.duckabary.core.user_management.UserService;

import java.util.ArrayList;

public class LibraryService {
    private final LibraryManagement libraryManagement;
    private final UserService userService;

    public LibraryService(LibraryManagement libraryManagement, UserService userService) {
        this.libraryManagement = libraryManagement;
        this.userService = userService;
    }

    public ArrayList<Document> getDocumentList() {
        return libraryManagement.getDocumentList();
    }

    public void addDocument(
            String title,
            String author,
            StringBuilder description,
            String publisher,
            int publishYear,
            String genre,
            String language,
            String ISBN,
            int quantity) {
        Document doc =
                new Document(
                        getNewDocumentID(),
                        title,
                        author,
                        description,
                        publisher,
                        publishYear,
                        genre,
                        language,
                        ISBN,
                        quantity);
        libraryManagement.addDocument(doc);
    }

    public void removeDocumentByID(int id) {
        libraryManagement.removeDocumentByID(id);
    }

    public Document getDocumentByID(int id) {
        return libraryManagement.getDocumentByID(id);
    }

    public ArrayList<Document> getDocumentByTitle(String title) {
        return libraryManagement.getDocumentsByTitle(title);
    }

    public ArrayList<Document> getDocumentByAuthor(String author) {
        return libraryManagement.getDocumentsByAuthor(author);
    }

    public ArrayList<Document> getDocumentByGenre(String genre) {
        return libraryManagement.getDocumentsByGenre(genre);
    }

    public boolean borrowDocumentByID(int userId, int docId) {
        if (getUserByID(userId) == null) {
            return false;
        }
        return libraryManagement.borrowDocumentByID(userId, docId);
    }

    public boolean returnDocumentByID(int userId, int docId) {
        return libraryManagement.returnDocumentByID(userId, docId);
    }

    public int getNewDocumentID() {
        return libraryManagement.generateNewDocumentID();
    }

    public User getUserByID(int userId) {
        return userService.getUser(userId);
    }

    public LibraryManagement getLibraryMgr() {
        return libraryManagement;
    }

    public UserService getUserService() {
        return userService;
    }
}
