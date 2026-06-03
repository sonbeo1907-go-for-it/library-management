package com.example.library.service;

public interface BookStatisticsService {
    long countTotalBooks();
    long countAvailableBooks();      // có quantity > 0
    long countBorrowedBooks();       // đang mượn (chưa trả)
    long countOverdueBooks();        // quá hạn chưa trả
    long countTotalUsers();
    long countActiveBorrowings();    // bản ghi đang mượn
}