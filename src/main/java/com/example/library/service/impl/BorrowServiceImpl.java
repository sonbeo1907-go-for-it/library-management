package com.example.library.service.impl;

import com.example.library.constant.ApplicationConstants;
import com.example.library.model.Book;
import com.example.library.model.BorrowRecord;
import com.example.library.model.BorrowStatus;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BorrowRepository;
import com.example.library.repository.UserRepository;
import com.example.library.service.BorrowService;
import com.example.library.service.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ValidationService validationService;

    public BorrowServiceImpl(BorrowRepository borrowRepository,
                             BookRepository bookRepository,
                             UserRepository userRepository,
                             ValidationService validationService) {
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.validationService = validationService;
    }

    @Override
    public BorrowRecord borrowBook(int bookId, int userId) {
        validationService.validateBookExists(bookId);
        validationService.validateUserExists(userId);
        validationService.validateBookAvailable(bookId);
        validationService.validateNotAlreadyBorrowed(bookId, userId);
        validationService.validateUserNotOverdue(userId);

        // Sau khi validate, lấy entity (chắc chắn tồn tại)
        Book book = bookRepository.findById(bookId).orElseThrow();
        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);

        BorrowRecord record = new BorrowRecord();
        record.setBook(book);
        record.setUser(userRepository.findById(userId).orElseThrow());
        record.setBorrowDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(ApplicationConstants.BORROW_DURATION_DAYS));
        record.setStatus(BorrowStatus.BORROWED);
        record.setFineAmount(BigDecimal.ZERO);

        return borrowRepository.save(record);
    }

    @Override
    public BorrowRecord returnBook(int recordId) {
        validationService.validateBorrowRecordExists(recordId);
        validationService.validateNotAlreadyReturned(recordId);

        BorrowRecord record = borrowRepository.findById(recordId).orElseThrow();
        record.setStatus(BorrowStatus.RETURNED);
        record.setReturnDate(LocalDate.now());

        if (record.getReturnDate().isAfter(record.getDueDate())) {
            long daysLate = ChronoUnit.DAYS.between(record.getDueDate(), record.getReturnDate());
            BigDecimal fine = BigDecimal.valueOf(daysLate * ApplicationConstants.FINE_PER_DAY);
            record.setFineAmount(fine);
        }

        Book book = record.getBook();
        book.setQuantity(book.getQuantity() + 1);
        bookRepository.save(book);

        return borrowRepository.save(record);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowRecord> getHistoryByUser(int userId) {
        return borrowRepository.findByUserIdOrderByBorrowDateDesc(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowRecord> getAllHistory() {
        return borrowRepository.findAllByOrderByBorrowDateDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowRecord> getOverdueRecords() {
        return borrowRepository.findByStatusAndDueDateBefore(BorrowStatus.BORROWED, LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public BorrowRecord findById(int id) {
        return borrowRepository.findById(id).orElse(null);
    }
}