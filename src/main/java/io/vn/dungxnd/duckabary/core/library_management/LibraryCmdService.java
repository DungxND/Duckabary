package io.vn.dungxnd.duckabary.core.library_management;

import io.vn.dungxnd.duckabary.core.user_management.UserCmdService;
import io.vn.dungxnd.duckabary.utils.LoggerUtils;

import java.util.ArrayList;

public class LibraryCmdService extends LibraryService {

    public LibraryCmdService(LibraryManagement libraryManagement, UserCmdService userCmdService) {
        super(libraryManagement, userCmdService);
        LoggerUtils.debug("LibraryCmdService initialized");
    }

    @Override
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

        Document doc = super.addDocument(
                title,
                author,
                description,
                publisher,
                publishYear,
                genre,
                language,
                ISBN,
                quantity);

        if (doc != null) {
            LoggerUtils.info("Document added successfully: " + title);
            System.out.println("Document added successfully");
        } else {
            LoggerUtils.warn("Failed to add document: " + title);
            System.out.println("Failed to add document");
        }
        return doc;
    }

    @Override
    public void removeDocumentByID(int id) {
        Document doc = super.getDocumentByID(id);
        if (doc == null) {
            LoggerUtils.warn("Attempted to remove non-existent document with ID: " + id);
            System.out.println("Document with id " + id + " not found");
            return;
        }
        super.removeDocumentByID(id);
        LoggerUtils.info("Document removed successfully: ID " + id);
        System.out.println("Document removed successfully");
    }

    @Override
    public Document getDocumentByID(int id) {
        Document doc = super.getDocumentByID(id);
        if (doc == null) {
            LoggerUtils.warn("Document not found with ID: " + id);
            System.out.println("Document with id " + id + " not found");
            return null;
        }
        LoggerUtils.debug("Retrieved document: " + doc.getTitle());
        return doc;
    }

    @Override
    public ArrayList<Document> getDocumentByTitle(String title) {
        LoggerUtils.debug("Searching documents by title: " + title);
        ArrayList<Document> documents = super.getDocumentByTitle(title);
        if (documents.isEmpty()) {
            LoggerUtils.info("No documents found with title: " + title);
            System.out.println("No document found with title " + title);
            return new ArrayList<>();
        }
        LoggerUtils.info("Found " + documents.size() + " documents with title: " + title);
        return documents;
    }

    @Override
    public ArrayList<Document> getDocumentByAuthor(String author) {
        LoggerUtils.debug("Searching documents by author: " + author);
        ArrayList<Document> documents = super.getDocumentByAuthor(author);
        if (documents.isEmpty()) {
            LoggerUtils.info("No documents found by author: " + author);
            System.out.println("No document found with author " + author);
            return new ArrayList<>();
        }
        LoggerUtils.info("Found " + documents.size() + " documents by author: " + author);
        return documents;
    }

    @Override
    public UserCmdService getUserService() {
        return (UserCmdService) super.getUserService();
    }

    public void displayDocumentInfo(Document doc) {
        if (doc == null) {
            LoggerUtils.warn("Attempted to display null document");
            return;
        }

        LoggerUtils.debug("Displaying info for document: " + doc.getTitle());
        System.out.println("======= Document ID: " + doc.getId() + " =======");
        System.out.println("Title: " + doc.getTitle());
        System.out.println("Author: " + doc.getAuthor());
        System.out.println("Description: " + doc.getDescription());
        System.out.println("Publisher: " + doc.getPublisher());
        System.out.println("Year: " + doc.getPublishYear());
        System.out.println("Genre: " + doc.getGenre());
        System.out.println("Language: " + doc.getLanguage());
        System.out.println("ISBN: " + doc.getISBN());
        System.out.println("Quantity: " + doc.getQuantity());
        System.out.println("================================");
    }
}
