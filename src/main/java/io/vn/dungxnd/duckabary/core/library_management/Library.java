package io.vn.dungxnd.duckabary.core.library_management;

import java.util.ArrayList;
import java.util.HashMap;

public class Library {
    private final HashMap<Integer, Document> documentList;

    public Library() {
        documentList = new HashMap<>();
    }

    public void addDocument(Document doc) {
        documentList.put(doc.getId(), doc);
    }

    public void removeDocument(int id) {
        documentList.remove(id);
    }

    public Document getDocument(int id) {
        return documentList.get(id);
    }

    public ArrayList<Document> getDocumentByName(String name) {
        ArrayList<Document> result = new ArrayList<>();
        for (Document doc : documentList.values()) {
            if (doc.getTitle().equalsIgnoreCase(name)) {
                result.add(doc);
            }
        }
        if (result.isEmpty()) {
            System.out.println("Document not found");
            return null;
        }
        return result;
    }

    public ArrayList<Document> getDocumentList() {
        return new ArrayList<>(documentList.values());
    }

    public ArrayList<Document> getDocumentByAuthor(String docAuthor) {
        ArrayList<Document> result = new ArrayList<>();
        for (Document doc : documentList.values()) {
            if (doc.getAuthor().equalsIgnoreCase(docAuthor)) {
                result.add(doc);
            }
        }
        if (result.isEmpty()) {
            System.out.println("Document not found");
            return null;
        }
        return result;
    }
}
