package io.vn.dungxnd.duckabary.infrastructure.repository.library;

import io.vn.dungxnd.duckabary.domain.model.library.Journal;

import java.util.List;
import java.util.Optional;

public interface JournalRepository {
    List<Journal> getAll();

    Optional<Journal> searchById(Long id);

    Journal save(Journal journal);

    List<Journal> searchByTitle(String title);

    Optional<Journal> searchByIssn(String issn);

    List<Journal> searchByAuthorId(Long authorId);
}
