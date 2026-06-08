package com.example.library.service.review;

import com.example.library.model.review.Review;
import com.example.library.model.review.ReviewDto;
import java.util.List;

public interface ReviewService {
    boolean hasUserReviewedBook(int userId, int bookId);
    Review addOrUpdateReview(int bookId, int userId, int rating, String comment);  // mới
    List<Review> getReviewsByBook(int bookId);
    double getAverageRating(int bookId);
    long getReviewCount(int bookId);
    List<ReviewDto> getReviewDtosByBook(int bookId);
    Review getReviewByUserAndBook(int userId, int bookId);  // mới
}