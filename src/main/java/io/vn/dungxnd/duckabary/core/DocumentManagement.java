package io.vn.dungxnd.duckabary.core;
import java.util.ArrayList;
import java.util.Scanner;

// chuwa xong 
public class DocumentManagement {
    private ArrayList<Document> docm;

    public DocumentManagement()
    {
        docm = new ArrayList<>();
    }
    public void addDocument(Document doc)
    {
        docm.add(doc);
    }

    public void removeDocument(String id)
    {
        docm.removeIf(doc -> doc.getId().equals(id));
    }

    public void borrowDOC(String docId)
    {
        for (Document doc : docm)
        {
            if (doc.getId().equals(docId))
            {
                if (doc.getQuantity() > 0)
                {
                    doc.setQuantity(doc.getQuantity() - 1);
                    System.out.println("Document borrowed successfully");
                }
                else
                {
                    System.out.println("Document is not available");
                }
                return;
            }
        }
        System.out.println("Document not found :))");
    }

    // chuaw xong
}
