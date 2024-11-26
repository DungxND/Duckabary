package io.vn.dungxnd.duckabary.infrastructure.repository.entity;

import io.vn.dungxnd.duckabary.domain.model.entity.Publisher;
import java.util.List;
import java.util.Optional;

public interface PublisherRepository {
    List<Publisher> findAll();

    Optional<Publisher> findById(Long id);

    Optional<Publisher> findByName(String name);

    List<Publisher> findByNamePattern(String namePattern);

    Publisher save(Publisher publisher);

    void delete(Long id);
}
