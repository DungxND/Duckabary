package io.vn.dungxnd.duckabary.core.library_management;

public class LibraryManagement {
    private Library library;

    public LibraryManagement() {
        library = new Library();
    }

    public Library getLibrary() {
        return library;
    }

    public void addDocument(Document doc) {
        library.addDocument(doc);
    }

    public void removeDocumentByID(int id) {
        library.removeDocument(id);
    }

    public void borrowDocumentByID(int docId) {
        Document doc = library.getDocument(docId);
        if (doc != null) {
            if (doc.getQuantity() > 0) {
                doc.setQuantity(doc.getQuantity() - 1);
                System.out.println("Document borrowed successfully");
            } else {
                System.out.println("Document is not available");
            }
        } else {
            System.out.println("Document not found");
        }
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

    public void syncDocument() {
        // TODO: Sync document with database
    }
}