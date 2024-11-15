package io.vn.dungxnd.duckabary.core.library_management;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Library {
    private final LinkedHashMap<Integer, Document> documentList;

    public Library() {
        documentList = new LinkedHashMap<>();
    }

    public void addDocument(Document doc) {
        documentList.put(documentList.size() + 1, doc);
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

    public LinkedHashMap<Integer, Document> getDocumentLkHashMap() {
        return documentList;
    }
}
