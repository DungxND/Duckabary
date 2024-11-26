package io.vn.dungxnd.duckabary.domain.model.entity;

import java.util.Optional;

public record Publisher(
        Long id,
        String name,
        Optional<String> email,
        Optional<String> phone,
        Optional<String> address) {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PHONE_REGEX = "^\\+?[0-9]{8,11}$";

    public Publisher {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Publisher name cannot be empty");
        }

        email.ifPresent(
                e -> {
                    if (!e.isEmpty() && !e.matches(EMAIL_REGEX)) {
                        throw new IllegalArgumentException("Invalid email format");
                    }
                });

        phone.ifPresent(
                p -> {
                    if (!p.isEmpty() && !p.matches(PHONE_REGEX)) {
                        throw new IllegalArgumentException("Invalid phone number format");
                    }
                });
    }

    public static Publisher createPublisher(
            Long id, String name, String email, String phone, String address) {
        return new Publisher(
                id,
                name,
                Optional.ofNullable(email).filter(e -> !e.isEmpty()),
                Optional.ofNullable(phone).filter(p -> !p.isEmpty()),
                Optional.ofNullable(address).filter(a -> !a.isEmpty()));
    }
}
