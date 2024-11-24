package io.vn.dungxnd.duckabary.infrastructure.repository;

import io.vn.dungxnd.duckabary.domain.model.user.Manager;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface ManagerRepository {
    List<Manager> findAll();

    Optional<Manager> findById(Connection conn, int id);

    Optional<Manager> findById(int id);

    Optional<Manager> findByUsername(String username);

    Optional<Manager> findByEmail(String email);

    Manager save(Manager admin);

    void delete(int id);

    boolean validateCredentials(String username, String password);
}
