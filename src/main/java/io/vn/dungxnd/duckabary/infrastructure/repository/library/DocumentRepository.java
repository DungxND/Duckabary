package io.vn.dungxnd.duckabary.infrastructure.repository.library;

import io.vn.dungxnd.duckabary.domain.model.library.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository {
    List<Document> findAll();

    Optional<Document> findById(Long id);

    void delete(Long id);

    List<Document> findByTitle(String title);

    List<Document> findByAuthorId(Long authorId);

    List<Document> findByAuthorName(String nameOrPenName);

    List<Document> findByType(String type);

    int countByType(String type);
}
