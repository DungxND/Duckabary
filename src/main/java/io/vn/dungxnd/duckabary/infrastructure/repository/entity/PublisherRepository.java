package io.vn.dungxnd.duckabary.infrastructure.repository.entity;

import io.vn.dungxnd.duckabary.domain.model.entity.Publisher;

import java.util.List;
import java.util.Optional;

public interface PublisherRepository {
    List<Publisher> getAll();

    Optional<Publisher> searchById(Long id);

    Optional<Publisher> searchByName(String name);

    List<Publisher> searchByNamePattern(String namePattern);

    Publisher save(Publisher publisher);

    void delete(Long id);
}
