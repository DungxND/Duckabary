package io.vn.dungxnd.duckabary.domain.service.user;

import io.vn.dungxnd.duckabary.domain.model.user.User;
import io.vn.dungxnd.duckabary.exception.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    /**
     * Get all users in database.
     *
     * @return List of users.
     */
    List<User> getAllUsers();

    /**
     * Get user by id.
     *
     * @param id User id.
     * @return User.
     * @throws DatabaseException If user not found.
     */
    User getUserById(int id) throws DatabaseException;

    /**
     * Save user to database.
     *
     * @param user User to save.
     * @return Saved user.
     * @throws DatabaseException If user is invalid.
     */
    User saveUser(User user) throws DatabaseException;

    /**
     * Delete user from database.
     *
     * @param id User id.
     * @throws DatabaseException If user not found.
     */
    void deleteUser(int id) throws DatabaseException;

    /**
     * Search user by name.
     *
     * @param name User name.
     * @return List of users.
     */
    List<User> searchByName(String name);

    /**
     * Find user by username.
     *
     * @param username User username.
     * @return User.
     */
    Optional<User> searchByUsername(String username);

    /**
     * Find user by email.
     *
     * @param email User email.
     * @return User.
     */
    Optional<User> searchByEmail(String email);

    /**
     * Check if username is available.
     *
     * @param username User username.
     * @return True if available, false otherwise.
     */
    boolean isUsernameAvailable(String username);

    /**
     * Check if email is available.
     *
     * @param email User email.
     * @return True if available, false otherwise.
     */
    boolean isEmailAvailable(String email);
}
