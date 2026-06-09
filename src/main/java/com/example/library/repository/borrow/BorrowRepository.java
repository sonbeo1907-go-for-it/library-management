package com.example.library.repository.borrow;

import com.example.library.model.borrow.BorrowRecord;
import com.example.library.model.borrow.BorrowStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<BorrowRecord, Integer> {

    // Lấy tất cả bản ghi của một user, sắp xếp theo ngày mượn giảm dần
    List<BorrowRecord> findByUserIdOrderByBorrowDateDesc(int userId);

    // Lấy tất cả bản ghi (sắp xếp giảm dần)
    List<BorrowRecord> findAllByOrderByBorrowDateDesc();

    // Lấy danh sách quá hạn (status = BORROWED và dueDate < ngày hiện tại)
    List<BorrowRecord> findByStatusAndDueDateBefore(BorrowStatus status, LocalDate date);

    // Kiểm tra xem user đã từng mượn và trả sách này chưa (để cho phép review)
    boolean existsByUserIdAndBookIdAndStatus(int userId, int bookId, BorrowStatus status);

    long countByStatus(BorrowStatus borrowStatus);

    long countByStatusAndDueDateBefore(BorrowStatus borrowStatus, LocalDate now);

    boolean existsByUserIdAndStatusAndDueDateBefore(int userId, BorrowStatus borrowStatus, LocalDate now);

    Optional<BorrowRecord> findByBorrowCode(String borrowCode);

    Page<BorrowRecord> findByUserIdOrderByBorrowDateDesc(int userId, Pageable pageable);
    Page<BorrowRecord> findAllByOrderByBorrowDateDesc(Pageable pageable);
    Page<BorrowRecord> findByStatusAndDueDateBefore(BorrowStatus status, LocalDate date, Pageable pageable);

    // Lịch sử của một user theo trạng thái
    Page<BorrowRecord> findByUserIdAndStatusOrderByBorrowDateDesc(int userId, BorrowStatus status, Pageable pageable);

    // Toàn bộ lịch sử theo trạng thái (cho Librarian)
    Page<BorrowRecord> findByStatusOrderByBorrowDateDesc(BorrowStatus status, Pageable pageable);

    @Query(value = "SELECT DATE(b.return_date) as date, SUM(b.fine_amount) as fineTotal " +
            "FROM borrow_record b WHERE b.status = 'RETURNED' AND b.return_date BETWEEN :start AND :end " +
            "GROUP BY DATE(b.return_date)", nativeQuery = true)
    List<Object[]> findFineRevenueByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(br) FROM BorrowRecord br WHERE br.returnedBy.id = :userId AND br.returnDate BETWEEN :start AND :end")
    long countReturnsByUser(@Param("userId") int userId, @Param("start") LocalDate start, @Param("end") LocalDate end);
}