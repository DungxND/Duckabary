package io.vn.dungxnd.duckabary.domain.model.library;

import java.time.LocalDateTime;
import java.util.Optional;

public record Thesis(
        Long id,
        String title,
        String author,
        String description,
        int publishYear,
        int quantity,
        String type,
        String university,
        String department,
        String supervisor,
        String degree,
        Optional<LocalDateTime> defenseDate)
        implements Document {}
