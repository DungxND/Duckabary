package io.vn.dungxnd.duckabary.infrastructure.repository.user;

import io.vn.dungxnd.duckabary.domain.model.user.User;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getAll();

    Optional<User> searchById(int id);

    Optional<User> searchById(Connection conn, int id);

    Optional<User> searchByUsername(String username);

    Optional<User> searchByEmail(String email);

    User save(User user);

    void delete(int id);

    List<User> searchByName(String name);
}
