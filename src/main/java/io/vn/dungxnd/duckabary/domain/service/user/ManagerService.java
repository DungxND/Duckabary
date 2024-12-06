package io.vn.dungxnd.duckabary.domain.service.user;

import io.vn.dungxnd.duckabary.domain.model.user.Manager;
import io.vn.dungxnd.duckabary.exception.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface ManagerService {
    /**
     * Get all managers in database.
     *
     * @return List of managers.
     */
    List<Manager> getAllManagers();

    /**
     * Get manager by id.
     *
     * @param id Manager id.
     * @return Manager.
     * @throws DatabaseException If manager not found.
     */
    Manager getManagerById(int id) throws DatabaseException;

    /**
     * Save manager to database.
     *
     * @param manager Manager to save.
     * @return Saved manager.
     * @throws DatabaseException If manager is invalid.
     */
    Manager saveManager(Manager manager) throws DatabaseException;

    /**
     * Delete manager from database.
     *
     * @param id Manager id.
     * @throws DatabaseException If manager not found.
     */
    void deleteManager(int id) throws DatabaseException;

    /**
     * Find manager by username.
     *
     * @param username Manager username.
     * @return Manager.
     */
    Optional<Manager> searchByUsername(String username);

    /**
     * Find manager by email.
     *
     * @param email Manager email.
     * @return Manager.
     */
    Optional<Manager> searchByEmail(String email);

    /**
     * Validate manager credentials.
     *
     * @param username Manager username.
     * @param password Manager password.
     * @return True if valid, false otherwise.
     */
    boolean isValidCredentials(String username, String password);

    /**
     * Authenticate manager.
     *
     * @param username Manager username.
     * @param password Manager password.
     * @return Manager.
     */
    Optional<Manager> authenticate(String username, String password);

    /**
     * Check if username is available.
     *
     * @param username Manager username.
     * @return True if available, false otherwise.
     */
    boolean isUsernameAvailable(String username);
}
