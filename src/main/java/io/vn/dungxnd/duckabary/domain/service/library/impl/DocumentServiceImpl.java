package io.vn.dungxnd.duckabary.domain.service.library.impl;

import io.vn.dungxnd.duckabary.domain.model.library.Book;
import io.vn.dungxnd.duckabary.domain.model.library.Document;
import io.vn.dungxnd.duckabary.domain.model.library.Journal;
import io.vn.dungxnd.duckabary.domain.model.library.Thesis;
import io.vn.dungxnd.duckabary.domain.service.library.DocumentService;
import io.vn.dungxnd.duckabary.exeption.DatabaseException;
import io.vn.dungxnd.duckabary.infrastructure.repository.library.BookRepository;
import io.vn.dungxnd.duckabary.infrastructure.repository.library.DocumentRepository;
import io.vn.dungxnd.duckabary.infrastructure.repository.library.JournalRepository;
import io.vn.dungxnd.duckabary.infrastructure.repository.library.ThesisRepository;
import io.vn.dungxnd.duckabary.util.TimeUtils;

import java.util.List;

public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final BookRepository bookRepository;
    private final JournalRepository journalRepository;
    private final ThesisRepository thesisRepository;

    public DocumentServiceImpl(
            DocumentRepository documentRepository,
            BookRepository bookRepository,
            JournalRepository journalRepository,
            ThesisRepository thesisRepository) {
        this.documentRepository = documentRepository;
        this.bookRepository = bookRepository;
        this.journalRepository = journalRepository;
        this.thesisRepository = thesisRepository;
    }

    @Override
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public Document getDocumentById(Long id) throws DatabaseException {
        return documentRepository
                .findById(id)
                .orElseThrow(() -> new DatabaseException("Document not found with id: " + id));
    }

    @Override
    public Document saveDocument(Document document) throws DatabaseException {
        return switch (document) {
            case Book book -> {
                validateBook(book);
                yield bookRepository.save(book);
            }
            case Journal journal -> {
                validateJournal(journal);
                yield journalRepository.save(journal);
            }
            case Thesis thesis -> {
                validateThesis(thesis);
                yield thesisRepository.save(thesis);
            }
            default -> throw new DatabaseException("Unknown document type");
        };
    }

    private void validateBook(Book book) {
        validateDocument(book);
        if (book.isbn() != null && !book.isbn().isEmpty()) {
            String cleanedIsbn = book.isbn().replaceAll("[-\\s]", "");
            if (cleanedIsbn.length() != 10 && cleanedIsbn.length() != 13) {
                throw new IllegalArgumentException("Invalid ISBN format. Must be 10 or 13 digits");
            }
        }

        if (book.language() != null && !book.language().isEmpty()) {
            if (!book.language().matches("^[a-z]{2}$")) {
                throw new IllegalArgumentException(
                        "Language code must be ISO-639-1 format (2 lowercase letters)");
            }
        }
    }

    private void validateJournal(Journal journal) {
        if (journal.issn() != null && !journal.issn().isEmpty()) {
            String cleanIssn = journal.issn().replaceAll("[-\\s]", "");
            if (cleanIssn.length() != 8) {
                throw new IllegalArgumentException("ISSN must be exactly 8 digits");
            }
        }
    }

    private void validateThesis(Thesis thesis) {
        validateDocument(thesis);
        if (thesis.defenseDate().isPresent()) {
            String dateStr = thesis.defenseDate().get().toString();

            if (!dateStr.matches("^\\d{4}-\\d{2}-\\d{2}(\\s+\\d{1,2}(:\\d{1,2})?)?$")) {
                throw new IllegalArgumentException(
                        "Invalid defense date format. Must be yyyy-MM-dd [HH[:mm]]");
            }

            String[] parts = dateStr.split("\\s+");
            if (parts.length > 1) {
                String time = parts[1];
                if (time.contains(":")) {
                    String[] timeParts = time.split(":");
                    int hours = Integer.parseInt(timeParts[0]);
                    int minutes = Integer.parseInt(timeParts[1]);

                    if (hours < 0 || hours > 23) {
                        throw new IllegalArgumentException("Hours must be between 0 and 23");
                    }
                    if (minutes < 0 || minutes > 59) {
                        throw new IllegalArgumentException("Minutes must be between 0 and 59");
                    }
                }
            }

            if (!TimeUtils.isValidDateTime(dateStr)) {
                throw new IllegalArgumentException("Invalid defense date");
            }
        }
    }

    @Override
    public void deleteDocument(Long id) throws DatabaseException {
        documentRepository
                .findById(id)
                .orElseThrow(() -> new DatabaseException("Document not found with id: " + id));
        documentRepository.delete(id);
    }

    @Override
    public List<Document> searchByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        return documentRepository.findByTitle(title.trim());
    }

    @Override
    public List<Document> searchByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty");
        }
        return documentRepository.findByAuthor(author.trim());
    }

    @Override
    public List<Document> getDocumentsByType(String type) {
        validateDocumentType(type);
        return documentRepository.findByType(type);
    }

    @Override
    public int getDocumentCountByType(String type) {
        validateDocumentType(type);
        return documentRepository.countByType(type);
    }

    @Override
    public boolean isDocumentAvailable(Long id) {
        Document document = getDocumentById(id);
        return document.quantity() > 0;
    }

    @Override
    public boolean canBeBorrowed(Long id, int quantity) {
        Document document = getDocumentById(id);
        return document.quantity() >= quantity;
    }

    @Override
    public void updateQuantity(Long id, int change) throws DatabaseException {
        Document document = getDocumentById(id);
        int newQuantity = document.quantity() + change;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Insufficient quantity available");
        }

        Document updatedDocument =
                switch (document) {
                    case Book book ->
                            new Book(
                                    book.id(),
                                    book.title(),
                                    book.author(),
                                    book.description(),
                                    book.publishYear(),
                                    newQuantity,
                                    book.type(),
                                    book.isbn(),
                                    book.publisher(),
                                    book.language(),
                                    book.genre());
                    case Journal journal ->
                            new Journal(
                                    journal.id(),
                                    journal.title(),
                                    journal.author(),
                                    journal.description(),
                                    journal.publishYear(),
                                    newQuantity,
                                    journal.type(),
                                    journal.issn(),
                                    journal.volume(),
                                    journal.issue());
                    case Thesis thesis ->
                            new Thesis(
                                    thesis.id(),
                                    thesis.title(),
                                    thesis.author(),
                                    thesis.description(),
                                    thesis.publishYear(),
                                    newQuantity,
                                    thesis.type(),
                                    thesis.university(),
                                    thesis.department(),
                                    thesis.supervisor(),
                                    thesis.degree(),
                                    thesis.defenseDate());
                };

        saveDocument(updatedDocument);
    }

    private void validateDocument(Document document) {
        if (document == null) {
            throw new IllegalArgumentException("Document cannot be null");
        }
        if (document.title() == null || document.title().trim().isEmpty()) {
            throw new IllegalArgumentException("Document title cannot be empty");
        }
        if (document.quantity() < 0) {
            throw new IllegalArgumentException("Document quantity cannot be negative");
        }
        validateDocumentType(document.type());
    }

    private void validateDocumentType(String type) {
        if (!List.of("BOOK", "JOURNAL", "THESIS").contains(type)) {
            throw new IllegalArgumentException("Invalid document type: " + type);
        }
    }
}
