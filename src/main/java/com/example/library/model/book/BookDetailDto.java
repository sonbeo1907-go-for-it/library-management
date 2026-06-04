package com.example.library.model.book;

import com.example.library.model.review.ReviewDto;

import java.util.List;

public class BookDetailDto {
    private Book book;
    private List<ReviewDto> reviews;
    private double averageRating;
    private long reviewCount;
    private boolean canReview;



    public BookDetailDto(Book book, List<ReviewDto> reviews, double averageRating, long reviewCount, boolean canReview) {
        this.book = book;
        this.reviews = reviews;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
        this.canReview = canReview;
    }

    public BookDetailDto() {
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public List<ReviewDto> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDto> reviews) {
        this.reviews = reviews;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public boolean isCanReview() {
        return canReview;
    }

    public void setCanReview(boolean canReview) {
        this.canReview = canReview;
    }

    public long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(long reviewCount) {
        this.reviewCount = reviewCount;
    }
}