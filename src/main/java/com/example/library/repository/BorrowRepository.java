package com.example.library.repository;

import com.example.library.model.BorrowRecord;
import com.example.library.model.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

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
}