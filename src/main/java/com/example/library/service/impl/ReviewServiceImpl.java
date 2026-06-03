package com.example.library.service.impl;

import com.example.library.dto.ReviewDto;
import com.example.library.model.Review;
import com.example.library.repository.BookRepository;
import com.example.library.repository.ReviewRepository;
import com.example.library.repository.UserRepository;
import com.example.library.service.ReviewService;
import com.example.library.service.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;    // để kiểm tra sách tồn tại
    private final UserRepository userRepository;    // để kiểm tra user tồn tại
    private final ValidationService validationService;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             BookRepository bookRepository,
                             UserRepository userRepository,
                             ValidationService validationService) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.validationService = validationService;
    }

    @Override
    public boolean hasUserReviewedBook(int userId, int bookId) {
        return reviewRepository.existsByUserIdAndBookId(userId, bookId);
    }

    @Override
    public Review addReview(int bookId, int userId, int rating, String comment) {
        validationService.validateCanAddReview(bookId, userId);

        Review review = new Review();
        review.setBook(bookRepository.findById(bookId).orElseThrow());
        review.setUser(userRepository.findById(userId).orElseThrow());
        review.setRating(rating);
        review.setComment(comment);
        return reviewRepository.save(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getReviewsByBook(int bookId) {
        return reviewRepository.findByBookIdOrderByCreatedAtDesc(bookId);
    }

    @Override
    @Transactional(readOnly = true)
    public double getAverageRating(int bookId) {
        Double avg = reviewRepository.getAverageRatingByBookId(bookId);
        return avg != null ? avg : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public long getReviewCount(int bookId) {
        return reviewRepository.countByBookId(bookId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewDtosByBook(int bookId) {
        List<Review> reviews = reviewRepository.findByBookIdOrderByCreatedAtDesc(bookId);
        return reviews.stream()
                .map(this::convertToDto)
                .toList();
    }

    private ReviewDto convertToDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setReviewerName(review.getUser().getFullName());
        return dto;
    }
}