package io.vn.dungxnd.duckabary.core.library_management;

import io.vn.dungxnd.duckabary.core.user_management.UserCmdService;

import java.util.ArrayList;

public class LibraryCmdService extends LibraryService {

    public LibraryCmdService(LibraryManagement libraryManagement, UserCmdService userCmdService) {
        super(libraryManagement, userCmdService);
    }

    @Override
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

        super.addDocument(
                title,
                author,
                description,
                publisher,
                publishYear,
                genre,
                language,
                ISBN,
                quantity);
        System.out.println("Document added successfully");
    }

    @Override
    public void removeDocumentByID(int id) {
        if (super.getDocumentByID(id) == null) {
            System.out.println("Document with id " + id + " not found");
            return;
        }
        super.removeDocumentByID(id);
        System.out.println("Document removed successfully");
    }

    @Override
    public Document getDocumentByID(int id) {
        Document doc = super.getDocumentByID(id);
        if (doc == null) {
            System.out.println("Document with id " + id + " not found");
            return null;
        }
        return doc;
    }

    @Override
    public ArrayList<Document> getDocumentByTitle(String title) {
        ArrayList<Document> documents = super.getDocumentByTitle(title);
        if (documents.isEmpty()) {
            System.out.println("No document found with title " + title);
            return null;
        }
        return documents;
    }

    @Override
    public ArrayList<Document> getDocumentByAuthor(String author) {
        ArrayList<Document> documents = super.getDocumentByAuthor(author);
        if (documents.isEmpty()) {
            System.out.println("No document found with author " + author);
            return null;
        }
        return documents;
    }

    @Override
    public UserCmdService getUserService() {
        return (UserCmdService) super.getUserService();
    }
}
