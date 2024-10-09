package io.vn.dungxnd.duckabary.core.library_management;

public class Document {
    private int id;
    private String title;
    private String author;
    private StringBuilder description;
    private String publisher;
    private int publishYear;
    private String ISBN;
    private int quantity;

    public Document(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = "";
        this.description = new StringBuilder();
        this.quantity = 0;
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
            String publisher,
            int publishYear,
            String ISBN,
            StringBuilder description,
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
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
        return description;
    }

    public void setDescription(StringBuilder description) {
        this.description = description;
    }

    public String getPublisher() {
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
}
