package io.vn.dungxnd.duckabary.domain.service.entity;

import io.vn.dungxnd.duckabary.domain.model.entity.Publisher;
import io.vn.dungxnd.duckabary.exception.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface PublisherService {
    /**
     * Get all publishers in database.
     *
     * @return List of publishers.
     */
    List<Publisher> getAllPublishers();

    /**
     * Get publisher by id.
     *
     * @param id Publisher id.
     * @return Publisher.
     * @throws DatabaseException If publisher not found.
     */
    Publisher getPublisherById(Long id) throws DatabaseException;

    /**
     * Get publisher by name.
     *
     * @param name Publisher name.
     * @return Publisher.
     */
    Optional<Publisher> getPublisherByName(String name);

    /**
     * Get publisher by name pattern.
     *
     * @param name Publisher name pattern.
     * @return List of publishers.
     */
    List<Publisher> getPublisherByNamePattern(String name);

    /**
     * Get publisher by email.
     *
     * @param publisher Publisher email.
     * @return Publisher.
     * @throws DatabaseException If publisher not found.
     */
    Publisher savePublisher(Publisher publisher) throws DatabaseException;

    /**
     * Delete publisher by id.
     *
     * @param id Publisher id.
     * @throws DatabaseException If publisher not found.
     */
    void deletePublisher(Long id) throws DatabaseException;

    /**
     * Find out if publisher exists by name else create new publisher.
     *
     * @param publisherName Publisher name.
     * @return Publisher.
     */
    Publisher findOrCreatePublisher(String publisherName);
}
