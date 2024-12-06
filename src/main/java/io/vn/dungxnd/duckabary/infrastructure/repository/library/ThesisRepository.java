package io.vn.dungxnd.duckabary.infrastructure.repository.library;

import io.vn.dungxnd.duckabary.domain.model.library.Thesis;

import java.util.List;
import java.util.Optional;

public interface ThesisRepository {
    List<Thesis> findAll();

    Optional<Thesis> searchById(Long id);

    Thesis save(Thesis thesis);

    List<Thesis> searchByTitle(String title);

    List<Thesis> searchByAuthorId(Long authorId);

    List<Thesis> searchByUniversity(String university);

    List<Thesis> searchBySupervisor(String supervisor);
}
