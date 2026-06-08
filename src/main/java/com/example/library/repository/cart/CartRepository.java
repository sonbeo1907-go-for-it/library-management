package com.example.library.repository.cart;

import com.example.library.model.cart.Cart;
import com.example.library.model.cart.CartStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    // Tìm giỏ ACTIVE của một user (mỗi user chỉ nên có 1 giỏ ACTIVE)
    Optional<Cart> findByUserIdAndStatus(int userId, CartStatus status);

    // Tìm giỏ SUBMITTED (chờ duyệt) – cho thủ thư
    List<Cart> findByStatusOrderBySubmittedAtAsc(CartStatus status);

    // Tìm giỏ theo mã code
    Optional<Cart> findByCartCode(String cartCode);

    // Lấy tất cả giỏ đã được duyệt bởi một thủ thư (tính lương)
    List<Cart> findByApprovedById(int userId);

    List<Cart> findByUserIdOrderByCreatedAtDesc(int userId);

    Page<Cart> findByStatusOrderBySubmittedAtAsc(CartStatus status, Pageable pageable);

    Page<Cart> findByUserIdOrderByCreatedAtDesc(int userId, Pageable pageable);

    @Query(value = "SELECT DATE(c.approved_at) as date, SUM(c.total_fee) as total " +
            "FROM cart c WHERE c.status = 'APPROVED' AND c.approved_at BETWEEN :start AND :end " +
            "GROUP BY DATE(c.approved_at)", nativeQuery = true)
    List<Object[]> findRentalRevenueByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}