package io.vn.dungxnd.duckabary.infrastructure.repository.entity;

import io.vn.dungxnd.duckabary.domain.model.entity.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    List<Author> getAll();

    Optional<Author> searchById(Long id);

    Optional<Author> searchByName(String name);

    List<Author> searchByNamePattern(String name);

    Optional<Author> searchByEmail(String email);

    List<Author> searchByEmailPattern(String email);

    Optional<Author> searchByPhone(String phone);

    List<Author> searchByPhonePattern(String phone);

    Author save(Author author);

    void delete(Long id);
}
