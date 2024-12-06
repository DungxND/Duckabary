package io.vn.dungxnd.duckabary.domain.service.library;

import io.vn.dungxnd.duckabary.domain.model.library.Book;
import io.vn.dungxnd.duckabary.domain.model.library.Document;
import io.vn.dungxnd.duckabary.exception.DatabaseException;

import java.util.List;

public interface DocumentService {
    /**
     * Get all documents in database.
     *
     * @return List of documents.
     */
    List<Document> getAllDocuments();

    /**
     * Get document by id.
     *
     * @param id Document id.
     * @return Document.
     * @throws DatabaseException If document not found.
     */
    Document getDocumentById(Long id) throws DatabaseException;

    /**
     * Save document to database.
     *
     * @param document Document to save.
     * @return Saved document.
     * @throws DatabaseException If document is invalid.
     */
    Document saveDocument(Document document) throws DatabaseException;

    /**
     * Save document with author to database.
     *
     * @param document Document to save.
     * @param authorName Author name.
     * @return Saved document.
     * @throws DatabaseException If document is invalid.
     */
    Document saveDocumentWithAuthor(Document document, String authorName) throws DatabaseException;

    /**
     * Delete document from database.
     *
     * @param id Document id.
     * @throws DatabaseException If document not found.
     */
    void deleteDocument(Long id) throws DatabaseException;

    /**
     * Search document by title.
     *
     * @param title Document title.
     * @return List of documents.
     */
    List<Document> searchByTitle(String title);

    /**
     * Search document by genre.
     *
     * @param genre Document genre.
     * @return List of documents.
     */
    List<Document> searchByGenre(String genre);

    /**
     * Search document by author id.
     *
     * @param authorId Author id.
     * @return List of documents.
     */
    List<Document> searchByAuthorId(Long authorId);

    /**
     * Search document by author name.
     *
     * @param authorName Author name.
     * @return List of documents.
     */
    List<Document> searchByAuthorName(String authorName);

    /**
     * Save book with publisher to database.
     *
     * @param book Book to save.
     * @param publisherName Publisher name.
     * @return Saved book.
     * @throws DatabaseException If book is invalid.
     */
    Document saveBookWithPublisher(Book book, String publisherName) throws DatabaseException;

    /**
     * Get documents by type.
     *
     * @param type Document type.
     * @return List of documents with specified type.
     */
    List<Document> getDocumentsByType(String type);

    /**
     * Get document count by type.
     *
     * @param type Document type.
     * @return Number of documents with specified type.
     */
    int getDocumentCountByType(String type);

    /**
     * Check if document is available.
     *
     * @param id Document id.
     * @return True if document is available, false otherwise.
     */
    boolean isDocumentAvailable(Long id);

    /**
     * Check if stock is enough to borrow.
     *
     * @param id Document id.
     * @param quantity Quantity to borrow.
     * @return True if stock is enough, false otherwise.
     */
    boolean isStockEnough(Long id, int quantity);

    /**
     * Update document quantity.
     *
     * @param id Document id.
     * @param change Quantity change.
     * @throws DatabaseException If document not found.
     */
    void updateQuantity(Long id, int change) throws DatabaseException;

    /**
     * Check if document type is valid.
     *
     * @param type Document type.
     * @return True if document type is valid, false otherwise.
     */
    boolean isValidDocumentType(String type);

    /**
     * Validate document.
     *
     * @param document Document to validate.
     * @throws IllegalArgumentException If document is invalid
     */
    void validateDocument(Document document) throws IllegalArgumentException;
}
