package com.example.library.service.review.impl;

import com.example.library.model.book.Book;
import com.example.library.model.user.User;
import com.example.library.service.book.BookService;
import com.example.library.service.review.ReviewPageService;
import com.example.library.service.review.ReviewService;
import com.example.library.service.user.CurrentUserService;
import com.example.library.service.validation.ValidationService;
import org.springframework.stereotype.Service;

@Service
public class ReviewPageServiceImpl implements ReviewPageService {

    private final ReviewService reviewService;
    private final BookService bookService;
    private final CurrentUserService currentUserService;
    private final ValidationService validationService;

    public ReviewPageServiceImpl(ReviewService reviewService, BookService bookService,
                                 CurrentUserService currentUserService, ValidationService validationService) {
        this.reviewService = reviewService;
        this.bookService = bookService;
        this.currentUserService = currentUserService;
        this.validationService = validationService;
    }

    @Override
    public Book prepareReviewForm(int bookId) {
        Book book = bookService.findById(bookId);
        if (book == null) {
            throw new RuntimeException("Không tìm thấy sách.");
        }
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("Bạn cần đăng nhập.");
        }
        validationService.validateCanAddReview(bookId, currentUser.getId());
        return book;
    }

    @Override
    public void addReview(int bookId, int rating, String comment) {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("Bạn cần đăng nhập.");
        }
        reviewService.addReview(bookId, currentUser.getId(), rating, comment);
    }
}