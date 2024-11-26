package io.vn.dungxnd.duckabary.domain.service.entity;

import io.vn.dungxnd.duckabary.domain.model.entity.Publisher;
import io.vn.dungxnd.duckabary.exception.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface PublisherService {
    List<Publisher> getAllPublishers();

    Publisher getPublisherById(Long id) throws DatabaseException;

    Optional<Publisher> getPublisherByName(String name);

    List<Publisher> getPublisherByNamePattern(String name);

    Publisher savePublisher(Publisher publisher) throws DatabaseException;

    void deletePublisher(Long id) throws DatabaseException;

    Publisher findOrCreatePublisher(String publisherName);
}
