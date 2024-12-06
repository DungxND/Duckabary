package io.vn.dungxnd.duckabary.domain.service.entity.impl;

import io.vn.dungxnd.duckabary.domain.model.entity.Author;
import io.vn.dungxnd.duckabary.domain.model.library.Document;
import io.vn.dungxnd.duckabary.domain.service.entity.AuthorService;
import io.vn.dungxnd.duckabary.exception.DatabaseException;
import io.vn.dungxnd.duckabary.infrastructure.repository.entity.AuthorRepository;
import io.vn.dungxnd.duckabary.infrastructure.repository.library.DocumentRepository;

import java.util.List;
import java.util.Optional;

public class AuthorServiceImpl implements AuthorService {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PHONE_REGEX = "^\\+?[0-9]{8,11}$";
    private static final int MIN_NAME_LENGTH = 2;

    private final AuthorRepository authorRepository;

    private final DocumentRepository documentRepository;

    public AuthorServiceImpl(
            AuthorRepository authorRepository, DocumentRepository documentRepository) {
        this.authorRepository = authorRepository;
        this.documentRepository = documentRepository;
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.getAll();
    }

    @Override
    public Author getAuthorById(Long id) throws DatabaseException {
        return authorRepository
                .searchById(id)
                .orElseThrow(() -> new DatabaseException("Author not found with id: " + id));
    }

    @Override
    public List<Author> getAuthorByNamePattern(String name) {
        return authorRepository.searchByNamePattern(name);
    }

    @Override
    public Optional<Author> getAuthorByName(String name) {
        validateName(name);
        return authorRepository.searchByName(name);
    }

    @Override
    public Optional<Author> getAuthorByEmail(String email) {
        validateEmail(email);
        return authorRepository.searchByEmail(email);
    }

    @Override
    public List<Author> getAuthorByEmailPattern(String email) {
        return authorRepository.searchByEmailPattern(email);
    }

    @Override
    public Optional<Author> getAuthorByPhone(String phone) {
        validatePhone(phone);
        return authorRepository.searchByPhone(phone);
    }

    @Override
    public List<Author> getAuthorByPhonePattern(String phone) {
        return authorRepository.searchByPhonePattern(phone);
    }

    @Override
    public Author saveAuthor(Author author) throws DatabaseException {
        validateAuthor(author);
        return authorRepository.save(author);
    }

    @Override
    public void deleteAuthor(Long id) throws DatabaseException {
        if (id == null) {
            throw new IllegalArgumentException("Author ID cannot be null");
        }

        Author author =
                authorRepository
                        .searchById(id)
                        .orElseThrow(
                                () -> new DatabaseException("Author not found with id: " + id));

        List<Document> documents = documentRepository.searchByAuthorId(id);
        if (!documents.isEmpty()) {
            throw new DatabaseException(
                    String.format(
                            "Cannot delete author %s (ID: %d). Author has %d associated documents",
                            author.fullName(), id, documents.size()));
        }

        authorRepository.delete(id);
    }

    @Override
    public Author findOrCreateAuthor(String authorName) throws DatabaseException {
        if (authorName == null || authorName.trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be empty");
        }

        Optional<Author> existingAuthor = authorRepository.searchByName(authorName.trim());
        if (existingAuthor.isPresent()) {
            return existingAuthor.get();
        }

        Author newAuthor =
                new Author(
                        null,
                        authorName.trim(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty());

        return authorRepository.save(newAuthor);
    }

    private void validateAuthor(Author author) {
        if (author == null) {
            throw new IllegalArgumentException("Author entity cannot be null");
        }
        if (author.email().isPresent()) {
            validateEmail(author.email().get());
        }
        if (author.phone().isPresent()) {
            validatePhone(author.phone().get());
        }
        validateName(author.fullName());
    }

    private void validateEmail(String email) {
        if (email != null && !email.isEmpty()) {
            if (!email.matches(EMAIL_REGEX)) {
                throw new IllegalArgumentException("Invalid email format");
            }
        }
    }

    private void validatePhone(String phone) {
        if (phone != null && !phone.isEmpty()) {
            if (!phone.matches(PHONE_REGEX)) {
                throw new IllegalArgumentException("Invalid phone number format");
            }
        }
    }

    private void validateName(String name) {
        if (name.length() < MIN_NAME_LENGTH) {
            throw new IllegalArgumentException("Author name must be at least 2 characters");
        }
    }
}
