package io.vn.dungxnd.duckabary.infrastructure.repository.library;

import io.vn.dungxnd.duckabary.domain.model.library.Thesis;

import java.util.List;
import java.util.Optional;

public interface ThesisRepository {
    List<Thesis> findAll();

    Optional<Thesis> findById(Long id);

    Thesis save(Thesis thesis);

    List<Thesis> findByTitle(String title);

    List<Thesis> findByAuthor(String author);

    List<Thesis> findByUniversity(String university);

    List<Thesis> findBySupervisor(String supervisor);
}
