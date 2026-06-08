package com.example.library.service.validation;

public interface ValidationService {
    void validateBookExists(int bookId);
    void validateUserExists(int userId);
    void validateBookAvailable(int bookId);
    void validateNotAlreadyBorrowed(int bookId, int userId); // đã mượn và chưa trả?
    void validateCanReview(int bookId, int userId);
    void validateUserNotOverdue(int userId); // có sách quá hạn không
    // Thêm vào interface ValidationService
    void validateBorrowRecordExists(int recordId);
    void validateNotAlreadyReturned(int recordId);
    void validateCanAddReview(int bookId, int userId);
    void validateCanReviewOrUpdate(int bookId, int userId);
}