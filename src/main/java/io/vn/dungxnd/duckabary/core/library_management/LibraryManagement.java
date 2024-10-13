package io.vn.dungxnd.duckabary.core.library_management;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class LibraryManagement {
    private final Library library;
    LibraryDatabaseManagement libraryDatabaseManagement;

    public LibraryManagement() {
        library = new Library();
        libraryDatabaseManagement = new LibraryDatabaseManagement();
        libraryDatabaseManagement.loadDocumentsFromDB().forEach((k, v) -> library.addDocument(v));
    }

    public Library getLibrary() {
        return library;
    }

    public Document getDocumentByID(int id) {
        return library.getDocument(id);
    }

    public ArrayList<Document> getDocumentByTitle(String title) {
        ArrayList<Document> documents = new ArrayList<>();
        for (Document doc : library.getDocumentList()) {
            if (doc.getTitle().toLowerCase().contains(title.toLowerCase())) {
                documents.add(doc);
            }
        }
        return documents;
    }

    public ArrayList<Document> getDocumentByAuthor(String author) {
        ArrayList<Document> documents = new ArrayList<>();
        for (Document doc : library.getDocumentList()) {
            if (doc.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                documents.add(doc);
            }
        }
        return documents;
    }

    public void addDocument(
          Document doc) {
        library.addDocument(doc);
        libraryDatabaseManagement.addDocumentToDB(doc);
    }

    public int getNewDocumentID() {
        return library.getDocumentList().size();
    }

    public void removeDocumentByID(int id) {
        library.removeDocument(id);
        libraryDatabaseManagement.removeDocumentFromDB(id);
        updateDocumentIDs();
    }

    public boolean borrowDocumentByID(int docId) {
        Document doc = library.getDocument(docId);
        if (doc != null) {
            if (doc.getQuantity() > 0) {
                doc.setQuantity(doc.getQuantity() - 1);
                return true;
            }
        }
        return false;
    }

    public void returnDocumentByID(int docId) {
        Document doc = library.getDocument(docId);
        if (doc != null) {
            doc.setQuantity(doc.getQuantity() + 1);
            System.out.println("Document returned successfully");
        } else {
            System.out.println("Document not found");
        }
    }

    public ArrayList<Document> getDocumentList() {
        return library.getDocumentList();
    }

    private void updateDocumentIDs() {
        LinkedHashMap<Integer, Document> documents = library.getDocumentListMap();
        int newId = 1;
        for (Document doc : documents.values()) {
            doc.setId(newId);
            libraryDatabaseManagement.updateDocumentIDInDB(doc.getId(), newId);
            newId++;
        }
    }
}
