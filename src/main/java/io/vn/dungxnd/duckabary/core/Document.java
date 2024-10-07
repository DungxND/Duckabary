package io.vn.dungxnd.duckabary.core;

public class Document {
    protected String title;
    protected String author;
    protected String id;
    protected int quantity;

    public Document(String title, String author, String id, int quantity)
    {
        this.title = title;
        this.author = author;
        this.id = id;
        this.quantity = quantity;
    }

    public String getTitle()
    {
        return title;
    }

    public String getAuthor()
    {
        return author;
    }

    public String getId()
    {
        return id;
    }

    public int getQuantity()
    {
        return quantity;
    }
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
}

