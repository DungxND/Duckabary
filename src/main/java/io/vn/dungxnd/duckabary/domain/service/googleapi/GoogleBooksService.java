package io.vn.dungxnd.duckabary.domain.service.googleapi;

import io.vn.dungxnd.duckabary.domain.model.library.Book;
import io.vn.dungxnd.duckabary.domain.model.library.Document;
import io.vn.dungxnd.duckabary.domain.service.entity.AuthorService;
import io.vn.dungxnd.duckabary.domain.service.entity.PublisherService;
import io.vn.dungxnd.duckabary.domain.service.library.DocumentService;
import io.vn.dungxnd.duckabary.exception.DatabaseException;

import javafx.scene.image.Image;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GoogleBooksService {
    private final GoogleBooksApiClient apiClient;
    private final DocumentService documentService;

    private final AuthorService authorService;

    private final PublisherService publisherService;

    public GoogleBooksService(
            DocumentService documentService,
            AuthorService authorService,
            PublisherService publisherService) {
        this.apiClient = new GoogleBooksApiClient(authorService, publisherService);
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.documentService = documentService;
    }

    public Document getDocumentByIdentifier(String identifier) throws Exception {
        return apiClient.searchDocumentByIdentifier(identifier);
    }

    public Image getDocumentImage(String identifier) {
        return apiClient.fetchDocumentImage(identifier);
    }

    public Document importDocumentByIdentifier(String identifier) throws Exception {
        Document document = apiClient.searchDocumentByIdentifier(identifier);
        if (document == null) {
            throw new IllegalArgumentException("Document not found with identifier: " + identifier);
        }
        return documentService.saveDocument(document);
    }

    public Book importBookByIsbn(String isbn, String authorName, String publisherName)
            throws Exception {
        Book book = apiClient.searchBookByIsbn(isbn);
        if (book == null) {
            throw new IllegalArgumentException("Book not found with ISBN: " + isbn);
        }
        return (Book)
                documentService.saveBookWithPublisher(
                        (Book) documentService.saveDocumentWithAuthor(book, authorName),
                        publisherName);
    }

    public List<Book> importBooksByTitle(String title, String authorName, String publisherName)
            throws Exception {
        List<Book> books = apiClient.searchBooks(title);
        return books.stream()
                .map(
                        book -> {
                            try {
                                return (Book)
                                        documentService.saveBookWithPublisher(
                                                (Book)
                                                        documentService.saveDocumentWithAuthor(
                                                                book, authorName),
                                                publisherName);
                            } catch (DatabaseException e) {
                                return null;
                            }
                        })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
