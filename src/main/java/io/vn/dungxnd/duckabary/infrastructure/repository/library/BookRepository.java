package io.vn.dungxnd.duckabary.infrastructure.repository.library;

import io.vn.dungxnd.duckabary.domain.model.library.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAll();

    Optional<Book> searchById(Long id);

    Book save(Book book);

    List<Book> searchByTitle(String title);

    Optional<Book> searchByIsbn(String isbn);

    List<Book> searchByAuthorId(Long authorId);

    List<Book> searchByPublisherId(Long publisherId);
}
