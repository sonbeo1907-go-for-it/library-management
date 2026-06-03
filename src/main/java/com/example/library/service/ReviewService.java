package com.example.library.service;

import com.example.library.model.Review;
import java.util.List;

public interface ReviewService {

    boolean hasUserReviewedBook(int userId, int bookId);

    Review addReview(int bookId, int userId, int rating, String comment);

    List<Review> getReviewsByBook(int bookId);

    double getAverageRating(int bookId);

    long getReviewCount(int bookId);
}