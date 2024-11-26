package io.vn.dungxnd.duckabary.domain.service.entity;

import io.vn.dungxnd.duckabary.domain.model.entity.Author;
import io.vn.dungxnd.duckabary.exception.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<Author> getAllAuthors();

    Author getAuthorById(Long id) throws DatabaseException;

    Optional<Author> getAuthorByName(String name);

    List<Author> getAuthorByNamePattern(String name);

    Optional<Author> getAuthorByEmail(String email);

    List<Author> getAuthorByEmailPattern(String email);

    Optional<Author> getAuthorByPhone(String phone);

    List<Author> getAuthorByPhonePattern(String phone);

    Author saveAuthor(Author author) throws DatabaseException;

    void deleteAuthor(Long id) throws DatabaseException;

    Optional<Author> findByEmail(String email);

    Author findOrCreateAuthor(String authorName);
}
