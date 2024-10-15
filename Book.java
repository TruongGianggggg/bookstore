package org.example.cuahangsach.model;

public class Book {
    private int id;
    private String title;
    private String author;
    private double price;
    private String imagePath;

    // Constructors
    public Book() {
    }

    public Book(String title, String author, double price, String imagePath) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.imagePath = imagePath;
    }

    public Book(int id, String title, String author, double price, String imagePath) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.imagePath = imagePath;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}

