package io.vn.dungxnd.duckabary.core.library_management;

import java.util.ArrayList;

public class LibraryCmdService extends LibraryService {

    public LibraryCmdService(LibraryManagement libraryManagement) {
        super(libraryManagement);
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

        super.addDocument(title, author, description, publisher, publishYear, genre, language, ISBN, quantity);
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
    public boolean borrowDocumentByID(int docId) {
        if (super.getDocumentByID(docId) == null) {
            System.out.println("Document with id " + docId + " not found");
            return false;
        }
        if (!super.borrowDocumentByID(docId)) {
            System.out.println("Document with id " + docId + " is out of stock");
            return false;
        }
        System.out.println("Document with id " + docId + " borrowed successfully");
        return true;
    }
}
