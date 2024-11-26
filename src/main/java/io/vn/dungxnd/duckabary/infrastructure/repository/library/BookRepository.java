package io.vn.dungxnd.duckabary.infrastructure.repository.library;

import io.vn.dungxnd.duckabary.domain.model.library.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAll();

    Optional<Book> findById(Long id);

    Book save(Book book);

    List<Book> findByTitle(String title);

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByAuthorId(Long authorId);

    List<Book> findByPublisherId(Long publisherId);
}
