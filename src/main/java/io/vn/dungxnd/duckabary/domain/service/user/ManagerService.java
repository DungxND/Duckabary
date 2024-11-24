package io.vn.dungxnd.duckabary.domain.service.user;

import io.vn.dungxnd.duckabary.domain.model.user.Manager;
import io.vn.dungxnd.duckabary.exeption.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface ManagerService {
    List<Manager> getAllManagers();

    Manager getManagerById(int id) throws DatabaseException;

    Manager saveManager(Manager manager) throws DatabaseException;

    void deleteManager(int id) throws DatabaseException;

    Optional<Manager> findByUsername(String username);

    Optional<Manager> findByEmail(String email);

    boolean validateCredentials(String username, String password);

    boolean isUsernameAvailable(String username);
}
