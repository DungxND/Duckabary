package io.vn.dungxnd.duckabary.core.library_management;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

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

    public ArrayList<Document> getDocumentsByTitle(String title) {
        return library.getDocumentList().stream()
                .filter(doc -> doc.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Document> getDocumentsByAuthor(String author) {
        return library.getDocumentList().stream()
                .filter(doc -> doc.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Document> getDocumentsByGenre(String genre) {
        return library.getDocumentList().stream()
                .filter(doc -> doc.getGenre().toLowerCase().contains(genre.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void addDocument(Document doc) {
        library.addDocument(doc);
        libraryDatabaseManagement.addDocumentToDB(doc);
    }

    public int generateNewDocumentID() {
        return library.getDocumentList().size();
    }

    public void removeDocumentByID(int id) {
        library.removeDocument(id);
        libraryDatabaseManagement.removeDocumentFromDB(id);
        updateDocumentIDs();
    }

    public ArrayList<Document> getDocumentList() {
        return library.getDocumentList();
    }

    private void updateDocumentIDs() {
        LinkedHashMap<Integer, Document> documents = library.getDocumentLkHashMap();
        int newId = 1;
        for (Document doc : documents.values()) {
            doc.setId(newId);
            libraryDatabaseManagement.updateDocumentIDInDB(doc.getId(), newId);
            newId++;
        }
    }

    public LibraryDatabaseManagement getLibraryDatabaseManagement() {
        return libraryDatabaseManagement;
    }

    public boolean isISBNExists(String isbn) {
        return library.getDocumentList().stream().anyMatch(doc -> doc.getISBN().equals(isbn));
    }
}
