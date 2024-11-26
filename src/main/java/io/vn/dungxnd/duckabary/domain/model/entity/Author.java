package io.vn.dungxnd.duckabary.domain.model.entity;

import java.time.LocalDate;
import java.util.Optional;

public record Author(
        Long id,
        String fullName,
        Optional<String> penName,
        Optional<String> email,
        Optional<String> phone,
        Optional<String> address,
        Optional<LocalDate> birthDate,
        Optional<LocalDate> deathDate) {
    public static Author createAuthor(
            Long id,
            String fullName,
            String penName,
            String email,
            String phone,
            String address,
            LocalDate birthDate,
            LocalDate deathDate) {
        return new Author(
                id,
                fullName,
                Optional.ofNullable(penName),
                Optional.ofNullable(email),
                Optional.ofNullable(phone),
                Optional.ofNullable(address),
                Optional.ofNullable(birthDate),
                Optional.ofNullable(deathDate));
    }

    public Author withId(Long newId) {
        return new Author(newId, fullName, penName, email, phone, address, birthDate, deathDate);
    }
}
