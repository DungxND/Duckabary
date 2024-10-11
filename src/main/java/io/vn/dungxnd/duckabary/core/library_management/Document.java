package io.vn.dungxnd.duckabary.core.library_management;

public class Document {
    private int id;
    private String title;
    private String author;
    private StringBuilder description;
    private String publisher;
    private String publishYear;
    private String genre;
    private String language;
    private String ISBN;
    private int quantity;

    public Document(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public Document(
            int id,
            String title,
            String author,
            String publisher,
            StringBuilder description,
            int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.description = description;
        this.quantity = quantity;
    }

    public Document(
            int id,
            String title,
            String author,
            StringBuilder description,
            String publisher,
            String publishYear,
            String ISBN,
            int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishYear = publishYear;
        this.ISBN = ISBN;
        this.description = description;
        this.quantity = quantity;
    }

    public Document(
            int id,
            String title,
            String author,
            StringBuilder description,
            String publisher,
            String publishYear,
            String genre,
            String language,
            String ISBN,
            int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishYear = publishYear;
        this.ISBN = ISBN;
        this.genre = genre;
        this.language = language;
        this.description = description;
        this.quantity = quantity;
    }

    public Document(
            String title,
            String author,
            String publisher,
            StringBuilder stringBuilder,
            int quantity) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.description = stringBuilder;
        this.quantity = quantity;
    }

    public String getTitle() {
        if (title == null) {
            return "Title is not available";
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        if (author == null) {
            return "Unknown author";
        }

        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StringBuilder getDescription() {
        if (description == null) {
            return new StringBuilder("Description is not available");
        }

        return description;
    }

    public void setDescription(StringBuilder description) {
        this.description = description;
    }

    public String getPublisher() {
        if (publisher == null) {
            return "Unknown publisher";
        }
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPublishYear() {
        if (publishYear == null) {
            return "Unknown publish year";
        }

        return publishYear;
    }

    public void setPublishYear(String publishYear) {
        this.publishYear = publishYear;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getGenre() {
        if (genre == null) {
            return "Unknown genre";
        }
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        if (language == null) {
            return "Unknown language";
        }
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
