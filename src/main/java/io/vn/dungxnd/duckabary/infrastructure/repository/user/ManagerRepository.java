package io.vn.dungxnd.duckabary.infrastructure.repository.user;

import io.vn.dungxnd.duckabary.domain.model.user.Manager;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface ManagerRepository {
    List<Manager> getAll();

    Optional<Manager> searchById(Connection conn, int id);

    Optional<Manager> searchById(int id);

    Optional<Manager> searchByUsername(String username);

    Optional<Manager> searchByEmail(String email);

    Manager save(Manager admin);

    void delete(int id);

    boolean validateCredentials(String username, String password);
}
