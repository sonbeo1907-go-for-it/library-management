package com.example.library.service.review;

import com.example.library.model.book.Book;
import com.example.library.model.user.User;

public interface ReviewPageService {
    Book prepareReviewForm(int bookId);
    void addOrUpdateReview(int bookId, int rating, String comment);
}