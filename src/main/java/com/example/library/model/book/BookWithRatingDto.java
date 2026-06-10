package com.example.library.model.book;

import java.math.BigDecimal;

public class BookWithRatingDto {
    private int id;
    private String title;
    private String author;
    private String imageFilename;
    private double averageRating;
    private int reviewCount;

    public BookWithRatingDto() {}

    // Getters và Setters đầy đủ
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getImageFilename() { return imageFilename; }
    public void setImageFilename(String imageFilename) { this.imageFilename = imageFilename; }
    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }
}