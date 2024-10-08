package io.vn.dungxnd.duckabary.core.library_management;

import java.util.ArrayList;
import java.util.HashMap;

public class Library {
    private HashMap<Integer, Document> documentList;

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

    public ArrayList<Document> getDocumentList() {
        return new ArrayList<>(documentList.values());
    }
}