package com.example.library.service.validation.impl;

import com.example.library.model.borrow.BorrowRecord;
import com.example.library.model.borrow.BorrowStatus;
import com.example.library.repository.book.BookRepository;
import com.example.library.repository.borrow.BorrowRepository;
import com.example.library.repository.review.ReviewRepository;
import com.example.library.repository.user.UserRepository;
import com.example.library.service.validation.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class ValidationServiceImpl implements ValidationService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowRepository borrowRepository;
    private final ReviewRepository reviewRepository;

    public ValidationServiceImpl(BookRepository bookRepository,
                                 UserRepository userRepository,
                                 BorrowRepository borrowRepository,
                                 ReviewRepository reviewRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.borrowRepository = borrowRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void validateBookExists(int bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new RuntimeException("Không tìm thấy sách id=" + bookId);
        }
    }

    @Override
    public void validateUserExists(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Không tìm thấy người dùng id=" + userId);
        }
    }

    @Override
    public void validateBookAvailable(int bookId) {
        validateBookExists(bookId);
        if (bookRepository.findById(bookId).orElseThrow().getQuantity() <= 0) {
            throw new RuntimeException("Sách đã hết, không thể mượn.");
        }
    }

    @Override
    public void validateNotAlreadyBorrowed(int bookId, int userId) {
        boolean alreadyBorrowed = borrowRepository.existsByUserIdAndBookIdAndStatus(userId, bookId, BorrowStatus.BORROWED);
        if (alreadyBorrowed) {
            throw new RuntimeException("Bạn đang mượn cuốn sách này và chưa trả.");
        }
    }

    @Override
    public void validateCanReview(int bookId, int userId) {
        validateBookExists(bookId);
        validateUserExists(userId);
        boolean hasReturned = borrowRepository.existsByUserIdAndBookIdAndStatus(userId, bookId, BorrowStatus.RETURNED);
        if (!hasReturned) {
            throw new RuntimeException("Bạn chỉ có thể đánh giá sách đã mượn và đã trả.");
        }
    }

    @Override
    public void validateUserNotOverdue(int userId) {
        boolean hasOverdue = borrowRepository.existsByUserIdAndStatusAndDueDateBefore(userId, BorrowStatus.BORROWED, LocalDate.now());
        if (hasOverdue) {
            throw new RuntimeException("Bạn có sách quá hạn chưa trả, không thể mượn thêm.");
        }
    }

    @Override
    public void validateBorrowRecordExists(int recordId) {
        if (!borrowRepository.existsById(recordId)) {
            throw new RuntimeException("Không tìm thấy bản ghi mượn id=" + recordId);
        }
    }

    @Override
    public void validateNotAlreadyReturned(int recordId) {
        BorrowRecord record = borrowRepository.findById(recordId).orElseThrow();
        if (record.getStatus() == BorrowStatus.RETURNED) {
            throw new RuntimeException("Sách này đã được trả rồi.");
        }
    }

    @Override
    public void validateCanAddReview(int bookId, int userId) {
        validateBookExists(bookId);
        validateUserExists(userId);
        // Kiểm tra đã từng mượn và trả sách
        if (!borrowRepository.existsByUserIdAndBookIdAndStatus(userId, bookId, BorrowStatus.RETURNED)) {
            throw new RuntimeException("Bạn chỉ có thể đánh giá sách đã mượn và đã trả.");
        }
        // Kiểm tra chưa review
        if (reviewRepository.existsByUserIdAndBookId(userId, bookId)) {
            throw new RuntimeException("Bạn đã đánh giá sách này rồi.");
        }
    }
}