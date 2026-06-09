package com.example.library.repository.review;

import com.example.library.model.review.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    Optional<Review> findByUserIdAndBookId(int userId, int bookId);

    @Query("SELECT b.id, b.title, b.author, b.imageFilename, AVG(r.rating), COUNT(r) " +
            "FROM Review r JOIN r.book b " +
            "WHERE b.isDeleted = false " +
            "GROUP BY b.id, b.title, b.author, b.imageFilename " +
            "ORDER BY AVG(r.rating) DESC")
    List<Object[]> findTopBooksByRating(Pageable pageable);
}