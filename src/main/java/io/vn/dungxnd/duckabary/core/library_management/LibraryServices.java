package io.vn.dungxnd.duckabary.core.library_management;

public class LibraryServices {
    private final LibraryManagement libraryManagement;

    public LibraryServices(LibraryManagement libraryManagement) {
        this.libraryManagement = libraryManagement;
    }

    public void addDocument(Document doc) {
        libraryManagement.addDocument(doc);
    }

    public void removeDocumentByID(int id) {
        libraryManagement.removeDocumentByID(id);
    }

    public void borrowDocumentByID(int docId) {
        libraryManagement.borrowDocumentByID(docId);
    }

    public void returnDocumentByID(int docId) {
        libraryManagement.returnDocumentByID(docId);
    }

    public void syncDocument() {
        libraryManagement.syncDocument();
    }
}