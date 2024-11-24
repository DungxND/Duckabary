package io.vn.dungxnd.duckabary.domain.model.library;

public record Book(
        Long id,
        String title,
        String author,
        String description,
        int publishYear,
        int quantity,
        String type,
        String isbn,
        String publisher,
        String language,
        String genre)
        implements Document {}
