package com.example.library.service.statistics;

public interface BookStatisticsService {
    long countTotalBooks();
    long countAvailableBooks();      // có quantity > 0
    long countBorrowedBooks();       // đang mượn (chưa trả)
    long countOverdueBooks();        // quá hạn chưa trả
    long countTotalUsers();
    long countActiveBorrowings();
    long countReaders(); // chỉ đếm READER, chưa xóa// bản ghi đang mượn
}