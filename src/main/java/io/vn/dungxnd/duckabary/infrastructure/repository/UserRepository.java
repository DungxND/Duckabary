package io.vn.dungxnd.duckabary.infrastructure.repository;

import io.vn.dungxnd.duckabary.domain.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();

    Optional<User> findById(int id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    User save(User user);

    void delete(int id);

    List<User> findByName(String name);
}
