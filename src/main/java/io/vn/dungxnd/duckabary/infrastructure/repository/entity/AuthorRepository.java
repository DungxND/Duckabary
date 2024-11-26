package io.vn.dungxnd.duckabary.infrastructure.repository.entity;

import io.vn.dungxnd.duckabary.domain.model.entity.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    List<Author> findAll();

    Optional<Author> findById(Long id);

    Optional<Author> findByName(String name);

    List<Author> findByNamePattern(String name);

    Optional<Author> findByEmail(String email);

    List<Author> findByEmailPattern(String email);

    Optional<Author> findByPhone(String phone);

    List<Author> findByPhonePattern(String phone);

    Author save(Author author);

    void delete(Long id);
}
