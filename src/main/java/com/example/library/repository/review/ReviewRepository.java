package com.example.library.repository.review;

import com.example.library.model.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    // Lấy tất cả đánh giá của một sách, mới nhất trước
    List<Review> findByBookIdOrderByCreatedAtDesc(int bookId);

    // Kiểm tra user đã đánh giá sách này chưa
    boolean existsByUserIdAndBookId(int userId, int bookId);

    // Tính điểm trung bình của sách
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
    Double getAverageRatingByBookId(@Param("bookId") int bookId);

    // Đếm số lượt đánh giá của sách
    long countByBookId(int bookId);
}