package io.vn.dungxnd.duckabary.domain.model.library;

public sealed interface Document permits Book, Thesis, Journal {
    Long id();

    String title();

    Long author_id();

    String description();

    int publishYear();

    int quantity();

    String type();
}
