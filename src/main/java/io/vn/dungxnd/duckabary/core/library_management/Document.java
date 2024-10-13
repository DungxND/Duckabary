package io.vn.dungxnd.duckabary.core.library_management;

public class Document {
    private int id;
    private String title;
    private String author;
    private StringBuilder description;
    private String publisher;
    private int publishYear;
    private String genre;
    private String language;
    private String ISBN;
    private int quantity;


    public Document(int nextDocumentID, String title, String author, StringBuilder description, String publisher, int publishYear, String genre, String language, String isbn, int quantity) {
        this.id = nextDocumentID;
        this.title = title;
        this.author = author;
        this.description = description;
        this.publisher = publisher;
        this.publishYear = publishYear;
        this.genre = genre;
        this.language = language;
        this.ISBN = isbn;
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

    public int getPublishYear() {
        if (publishYear == 0) {
            return -1;
        }

        return publishYear;
    }

    public void setPublishYear(int publishYear) {
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
