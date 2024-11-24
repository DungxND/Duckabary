package io.vn.dungxnd.duckabary.domain.service.user;

import io.vn.dungxnd.duckabary.domain.model.user.User;
import io.vn.dungxnd.duckabary.exeption.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();

    User getUserById(int id) throws DatabaseException;

    User saveUser(User user) throws DatabaseException;

    void deleteUser(int id) throws DatabaseException;

    List<User> searchByName(String name);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean isUsernameAvailable(String username);

    boolean isEmailAvailable(String email);
}
