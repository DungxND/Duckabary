package io.vn.dungxnd.duckabary.domain.service.library;

import io.vn.dungxnd.duckabary.domain.model.library.Book;
import io.vn.dungxnd.duckabary.domain.model.library.Document;
import io.vn.dungxnd.duckabary.exception.DatabaseException;

import java.util.List;

public interface DocumentService {
    List<Document> getAllDocuments();

    Document getDocumentById(Long id) throws DatabaseException;

    Document saveDocument(Document document) throws DatabaseException;

    Document saveDocumentWithAuthor(Document document, String authorName) throws DatabaseException;

    void deleteDocument(Long id) throws DatabaseException;

    List<Document> searchByTitle(String title);

    List<Document> searchByAuthorId(Long authorId);

    List<Document> searchByAuthorName(String authorName);

    Document saveBookWithPublisher(Book book, String publisherName) throws DatabaseException;

    List<Document> getDocumentsByType(String type);

    int getDocumentCountByType(String type);

    boolean isDocumentAvailable(Long id);

    boolean canBeBorrowed(Long id, int quantity);

    void updateQuantity(Long id, int change) throws DatabaseException;

    boolean isValidDocumentType(String type);

    void validateDocument(Document document) throws IllegalArgumentException;
}
