package io.vn.dungxnd.duckabary.domain.model.library;

public record Book(
        Long id,
        String title,
        Long author_id,
        String description,
        int publishYear,
        int quantity,
        String type,
        String isbn,
        Long publisher_id,
        String language,
        String genre)
        implements Document {
    public static Book createBook(
            Long id,
            String title,
            Long authorId,
            String description,
            int publishYear,
            int quantity,
            String isbn,
            Long publisherId,
            String language,
            String genre) {
        return new Book(
                id,
                title,
                authorId,
                description,
                publishYear,
                quantity,
                "BOOK",
                isbn,
                publisherId,
                language,
                genre);
    }
}
