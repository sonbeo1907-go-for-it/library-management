package com.example.library.service.book.impl;

import com.example.library.model.book.Book;
import com.example.library.model.book.BookDetailDto;
import com.example.library.model.user.User;
import com.example.library.service.book.BookPageService;
import com.example.library.service.book.BookService;
import com.example.library.service.review.ReviewService;
import com.example.library.service.user.CurrentUserService;
import com.example.library.service.validation.ValidationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class BookPageServiceImpl implements BookPageService {

    private final BookService bookService;
    private final ReviewService reviewService;
    private final CurrentUserService currentUserService;
    private final ValidationService validationService;

    public BookPageServiceImpl(BookService bookService, ReviewService reviewService,
                               CurrentUserService currentUserService, ValidationService validationService) {
        this.bookService = bookService;
        this.reviewService = reviewService;
        this.currentUserService = currentUserService;
        this.validationService = validationService;
    }

    @Override
    public List<Book> getBooksForList(String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            return bookService.search(keyword);
        }
        return bookService.findAll();
    }

    @Override
    public BookDetailDto getBookDetail(int bookId) {
        Book book = bookService.findById(bookId);
        if (book == null) return null;

        var reviews = reviewService.getReviewDtosByBook(bookId);
        double avg = reviewService.getAverageRating(bookId);
        long count = reviewService.getReviewCount(bookId);

        User currentUser = currentUserService.getCurrentUser();
        boolean canReview = false;
        if (currentUser != null) {
            try {
                validationService.validateCanReviewOrUpdate(bookId, currentUser.getId());
                canReview = true;
            } catch (RuntimeException ignored) {}
        }

        return new BookDetailDto(book, reviews, avg, count, canReview);
    }

    @Override
    public Book saveBook(Book book, MultipartFile imageFile) throws IOException {
        return bookService.save(book, imageFile);
    }

    @Override
    public Book updateBook(int id, Book bookDetails, MultipartFile imageFile) throws IOException {
        return bookService.update(id, bookDetails, imageFile);
    }

    @Override
    public void deleteBook(int id) {
        bookService.deleteById(id);
    }

    @Override
    public Book getBookById(int id) {
        return bookService.findById(id);
    }

    @Override
    public Page<Book> getBooksForList(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookService.searchBooks(keyword, pageable);
    }
}