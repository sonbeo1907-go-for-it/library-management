// File: src/main/java/com/example/library/dto/BookDto.java
package com.example.library.model.book;

public class BookDto {
    private int id;
    private String title;
    private String author;
    private String publisher;
    private int year;
    private int quantity;
    private String imageFilename;

    public BookDto() {}

    public BookDto(int id, String title, String author, String publisher, int year, int quantity, String imageFilename) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.quantity = quantity;
        this.imageFilename = imageFilename;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getImageFilename() { return imageFilename; }
    public void setImageFilename(String imageFilename) { this.imageFilename = imageFilename; }
}