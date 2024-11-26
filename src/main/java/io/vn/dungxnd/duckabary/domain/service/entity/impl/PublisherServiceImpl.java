package io.vn.dungxnd.duckabary.domain.service.entity.impl;

import io.vn.dungxnd.duckabary.domain.model.entity.Publisher;
import io.vn.dungxnd.duckabary.domain.model.library.Book;
import io.vn.dungxnd.duckabary.domain.service.entity.PublisherService;
import io.vn.dungxnd.duckabary.exception.DatabaseException;
import io.vn.dungxnd.duckabary.infrastructure.repository.entity.PublisherRepository;
import io.vn.dungxnd.duckabary.infrastructure.repository.library.BookRepository;

import java.util.List;
import java.util.Optional;

public class PublisherServiceImpl implements PublisherService {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PHONE_REGEX = "^\\+?[0-9]{8,11}$";

    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;

    public PublisherServiceImpl(
            PublisherRepository publisherRepository, BookRepository bookRepository) {
        this.publisherRepository = publisherRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    @Override
    public Publisher getPublisherById(Long id) throws DatabaseException {
        return publisherRepository
                .findById(id)
                .orElseThrow(() -> new DatabaseException("Publisher not found with id: " + id));
    }

    @Override
    public Optional<Publisher> getPublisherByName(String name) {
        validateName(name);
        return publisherRepository.findByName(name);
    }

    @Override
    public List<Publisher> getPublisherByNamePattern(String name) {
        return publisherRepository.findByNamePattern(name);
    }

    @Override
    public Publisher savePublisher(Publisher publisher) throws DatabaseException {
        validatePublisher(publisher);
        return publisherRepository.save(publisher);
    }

    @Override
    public void deletePublisher(Long id) throws DatabaseException {
        if (id == null) {
            throw new IllegalArgumentException("Publisher ID cannot be null");
        }

        Publisher publisher =
                publisherRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new DatabaseException("Publisher not found with id: " + id));

        List<Book> books = bookRepository.findByPublisherId(id);
        if (!books.isEmpty()) {
            throw new DatabaseException(
                    String.format(
                            "Cannot delete publisher_id %s (ID: %d). Publisher has %d associated books",
                            publisher.name(), id, books.size()));
        }

        publisherRepository.delete(id);
    }

    @Override
    public Publisher findOrCreatePublisher(String publisherName) {
        if (publisherName == null || publisherName.trim().isEmpty()) {
            throw new IllegalArgumentException("Publisher name cannot be empty");
        }

        Optional<Publisher> existingPublisher =
                publisherRepository.findByName(publisherName.trim());
        if (existingPublisher.isPresent()) {
            return existingPublisher.get();
        }

        Publisher newPublisher =
                new Publisher(
                        null,
                        publisherName.trim(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty());
        return publisherRepository.save(newPublisher);
    }

    private void validatePublisher(Publisher publisher) {
        if (publisher == null) {
            throw new IllegalArgumentException("Publisher cannot be null");
        }
        validateName(publisher.name());
        publisher.email().ifPresent(this::validateEmail);
        publisher.phone().ifPresent(this::validatePhone);
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Publisher name cannot be empty");
        }
    }

    private void validateEmail(String email) {
        if (!email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    private void validatePhone(String phone) {
        if (!phone.matches(PHONE_REGEX)) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
    }
}
