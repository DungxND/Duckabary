// package io.vn.dungxnd.duckabary.domain.service.googleapi;
//
// import io.vn.dungxnd.duckabary.domain.model.library.Book;
// import io.vn.dungxnd.duckabary.domain.service.library.DocumentService;
// import io.vn.dungxnd.duckabary.exception.DatabaseException;
//
// import java.util.List;
// import java.util.Objects;
// import java.util.stream.Collectors;
//
// public class GoogleBooksService {
//    private final GoogleBooksApiClient apiClient;
//    private final DocumentService documentService;
//
//    public GoogleBooksService(DocumentService documentService) {
//        this.apiClient = new GoogleBooksApiClient();
//        this.documentService = documentService;
//    }
//
//    public Book importBookByIsbn(String isbn, String authorName, String publisherName)
//            throws Exception {
//        Book book = apiClient.searchBookByIsbn(isbn);
//        if (book == null) {
//            throw new IllegalArgumentException("Book not found with ISBN: " + isbn);
//        }
//        return (Book)
//                documentService.saveBookWithPublisher(
//                        (Book) documentService.saveDocumentWithAuthor(book, authorName),
//                        publisherName);
//    }
//
//    public List<Book> importBooksByTitle(String title, String authorName, String publisherName)
//            throws Exception {
//        List<Book> books = apiClient.searchBooks(title);
//        return books.stream()
//                .map(
//                        book -> {
//                            try {
//                                return (Book)
//                                        documentService.saveBookWithPublisher(
//                                                (Book)
//                                                        documentService.saveDocumentWithAuthor(
//                                                                book, authorName),
//                                                publisherName);
//                            } catch (DatabaseException e) {
//                                return null;
//                            }
//                        })
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//    }
// }
