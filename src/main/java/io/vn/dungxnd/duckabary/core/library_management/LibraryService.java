package io.vn.dungxnd.duckabary.core.library_management;

import io.vn.dungxnd.duckabary.core.user_management.User;
import io.vn.dungxnd.duckabary.core.user_management.UserService;
import io.vn.dungxnd.duckabary.utils.LoggerUtils;

import java.util.ArrayList;

public class LibraryService {
    private final LibraryManagement libraryManagement;
    private final UserService userService;

    public LibraryService(LibraryManagement libraryManagement, UserService userService) {
        this.libraryManagement = libraryManagement;
        this.userService = userService;
        LoggerUtils.debug("LibraryService initialized");
    }

    public ArrayList<Document> getDocumentList() {
        LoggerUtils.debug("Retrieving all documents");
        ArrayList<Document> documents = libraryManagement.getDocumentList();
        LoggerUtils.info("Retrieved " + documents.size() + " documents");
        return documents;
    }

    public Document addDocument(
            String title,
            String author,
            StringBuilder description,
            String publisher,
            int publishYear,
            String genre,
            String language,
            String ISBN,
            int quantity) {
        try {
            validateDocumentInput(title, author, ISBN, quantity);

            int docId = getNewDocumentID();
            Document doc = new Document(
                    docId,
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
            LoggerUtils.info("Added new document: " + title + " (ID: " + docId + ")");
            return doc;
        } catch (IllegalArgumentException e) {
            LoggerUtils.error("Failed to add document: " + title + ". " + e.getMessage(), e);
            return null;
        }
    }

    private void validateDocumentInput(String title, String author, String ISBN, int quantity) {
        if (title == null || title.isBlank()) {
            LoggerUtils.warn("Attempted to add document with empty title");
            throw new IllegalArgumentException("Title cannot be empty");
        }

        if (quantity < 0) {
            LoggerUtils.warn("Attempted to add document with negative quantity: " + quantity);
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        if (ISBN != null && !ISBN.isBlank() && libraryManagement.isISBNExists(ISBN)) {
            LoggerUtils.warn("Attempted to add document with existing ISBN: " + ISBN);
            throw new IllegalArgumentException("ISBN already exists");
        }
    }

    public void removeDocumentByID(int id) {
        Document doc = libraryManagement.getDocumentByID(id);
        if (doc == null) {
            LoggerUtils.warn("Attempted to remove non-existent document with ID: " + id);
            return;
        }
        libraryManagement.removeDocumentByID(id);
        LoggerUtils.info("Removed document: " + doc.getTitle() + " (ID: " + id + ")");
    }

    public Document getDocumentByID(int id) {
        LoggerUtils.debug("Retrieving document with ID: " + id);
        Document doc = libraryManagement.getDocumentByID(id);
        if (doc == null) {
            LoggerUtils.warn("Document not found with ID: " + id);
        }
        return doc;
    }

    public ArrayList<Document> getDocumentByTitle(String title) {
        if (title == null || title.isBlank()) {
            LoggerUtils.warn("Attempted to search documents with empty title");
            return new ArrayList<>();
        }
        LoggerUtils.debug("Searching documents by title: " + title);
        ArrayList<Document> docs = libraryManagement.getDocumentsByTitle(title);
        LoggerUtils.info("Found " + docs.size() + " documents matching title: " + title);
        return docs;
    }

    public ArrayList<Document> getDocumentByAuthor(String author) {
        if (author == null || author.isBlank()) {
            LoggerUtils.warn("Attempted to search documents with empty author");
            return new ArrayList<>();
        }
        LoggerUtils.debug("Searching documents by author: " + author);
        ArrayList<Document> docs = libraryManagement.getDocumentsByAuthor(author);
        LoggerUtils.info("Found " + docs.size() + " documents by author: " + author);
        return docs;
    }

    public ArrayList<Document> getDocumentByGenre(String genre) {
        if (genre == null || genre.isBlank()) {
            LoggerUtils.warn("Attempted to search documents with empty genre");
            return new ArrayList<>();
        }
        LoggerUtils.debug("Searching documents by genre: " + genre);
        ArrayList<Document> docs = libraryManagement.getDocumentsByGenre(genre);
        LoggerUtils.info("Found " + docs.size() + " documents in genre: " + genre);
        return docs;
    }

    public int getNewDocumentID() {
        return libraryManagement.generateNewDocumentID();
    }

    public LibraryManagement getLibraryMgr() {
        return libraryManagement;
    }

    public UserService getUserService() {
        return userService;
    }
}
