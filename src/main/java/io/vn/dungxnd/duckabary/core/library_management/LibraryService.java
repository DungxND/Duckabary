package io.vn.dungxnd.duckabary.core.library_management;

import java.util.ArrayList;

public class LibraryService {
    private final LibraryManagement libraryManagement;

    public LibraryService(LibraryManagement libraryManagement) {
        this.libraryManagement = libraryManagement;
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
        return libraryManagement.getDocumentByTitle(title);
    }

    public ArrayList<Document> getDocumentByAuthor(String author) {
        return libraryManagement.getDocumentByAuthor(author);
    }

    public boolean borrowDocumentByID(int docId) {
        return libraryManagement.borrowDocumentByID(docId);
    }

    public void returnDocumentByID(int docId) {
        libraryManagement.returnDocumentByID(docId);
    }

    public int getNewDocumentID() {
        return libraryManagement.getNewDocumentID();
    }
}
