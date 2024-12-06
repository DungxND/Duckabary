package io.vn.dungxnd.duckabary.domain.service.entity;

import io.vn.dungxnd.duckabary.domain.model.entity.Author;
import io.vn.dungxnd.duckabary.exception.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    /**
     * Get all authors in database.
     *
     * @return List of authors.
     */
    List<Author> getAllAuthors();

    /**
     * Get author by id.
     *
     * @param id Author id.
     * @return Author.
     * @throws DatabaseException If author not found.
     */
    Author getAuthorById(Long id) throws DatabaseException;

    /**
     * Get author by name.
     *
     * @param name Author name.
     * @return Author.
     */
    Optional<Author> getAuthorByName(String name);

    /**
     * Get author by name pattern.
     *
     * @param name Author name pattern.
     * @return List of authors.
     */
    List<Author> getAuthorByNamePattern(String name);

    /**
     * Get author by email.
     *
     * @param email Author email.
     * @return Author.
     */
    Optional<Author> getAuthorByEmail(String email);

    /**
     * Get author by email pattern.
     *
     * @param email Author email pattern.
     * @return List of authors.
     */
    List<Author> getAuthorByEmailPattern(String email);

    /**
     * Get author by phone.
     *
     * @param phone Author phone.
     * @return Author.
     */
    Optional<Author> getAuthorByPhone(String phone);

    /**
     * Get author by phone pattern.
     *
     * @param phone Author phone pattern.
     * @return List of authors.
     */
    List<Author> getAuthorByPhonePattern(String phone);

    /**
     * Save author.
     *
     * @param author Author.
     * @return Author.
     * @throws DatabaseException If author not saved.
     */
    Author saveAuthor(Author author) throws DatabaseException;

    /**
     * Delete author by id.
     *
     * @param id Author id.
     * @throws DatabaseException If author not deleted.
     */
    void deleteAuthor(Long id) throws DatabaseException;

    /**
     * Find author by author name if not exist create new author.
     *
     * @param authorName Author name.
     * @return Author.
     */
    Author findOrCreateAuthor(String authorName);
}
