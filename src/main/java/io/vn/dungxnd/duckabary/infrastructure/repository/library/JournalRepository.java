package io.vn.dungxnd.duckabary.infrastructure.repository.library;

import io.vn.dungxnd.duckabary.domain.model.library.Book;
import io.vn.dungxnd.duckabary.domain.model.library.Journal;

import java.util.List;
import java.util.Optional;

public interface JournalRepository {
    List<Journal> findAll();

    Optional<Journal> findById(Long id);

    Journal save(Journal journal);

    List<Journal> findByTitle(String title);

    Optional<Journal> findByIssn(String issn);

    List<Journal> findByAuthorId(Long authorId);
}
