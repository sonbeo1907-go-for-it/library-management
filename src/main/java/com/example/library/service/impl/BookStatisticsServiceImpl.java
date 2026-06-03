package com.example.library.service.impl;

import com.example.library.model.BorrowStatus;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BorrowRepository;
import com.example.library.repository.UserRepository;
import com.example.library.service.BookStatisticsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class BookStatisticsServiceImpl implements BookStatisticsService {

    private final BookRepository bookRepository;
    private final BorrowRepository borrowRepository;
    private final UserRepository userRepository;

    public BookStatisticsServiceImpl(BookRepository bookRepository,
                                     BorrowRepository borrowRepository,
                                     UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.borrowRepository = borrowRepository;
        this.userRepository = userRepository;
    }

    @Override
    public long countTotalBooks() {
        return bookRepository.countByIsDeletedFalse();
    }

    @Override
    public long countAvailableBooks() {
        return bookRepository.countByQuantityGreaterThanAndIsDeletedFalse(0);
    }

    @Override
    public long countBorrowedBooks() {
        // Số bản ghi đang mượn (chưa trả)
        return borrowRepository.countByStatus(BorrowStatus.BORROWED);
    }

    @Override
    public long countOverdueBooks() {
        return borrowRepository.countByStatusAndDueDateBefore(BorrowStatus.BORROWED, LocalDate.now());
    }

    @Override
    public long countTotalUsers() {
        return userRepository.countByIsDeletedFalse();
    }

    @Override
    public long countActiveBorrowings() {
        return countBorrowedBooks(); // tương tự, có thể tách riêng logic nếu cần
    }
}