package io.vn.dungxnd.duckabary.infrastructure.repository.library;

import io.vn.dungxnd.duckabary.domain.model.library.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository {
    List<Document> findAll();

    Optional<Document> searchById(Long id);

    void delete(Long id);

    Optional<Document> checkExistByIdentifier(String identifier);

    List<Document> searchByTitle(String title);

    List<Document> searchByGenre(String genre);

    List<Document> searchByAuthorId(Long authorId);

    List<Document> searchByAuthorName(String nameOrPenName);

    List<Document> searchByType(String type);

    int countByType(String type);
}
