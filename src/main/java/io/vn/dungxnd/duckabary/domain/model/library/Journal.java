package io.vn.dungxnd.duckabary.domain.model.library;

public record Journal(
        Long id,
        String title,
        String author,
        String description,
        int publishYear,
        int quantity,
        String type,
        String issn,
        String volume,
        String issue)
        implements Document {}
