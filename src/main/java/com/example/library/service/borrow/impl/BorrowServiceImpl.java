package com.example.library.service.borrow.impl;

import com.example.library.constant.ApplicationConstants;
import com.example.library.model.borrow.BorrowRecordDto;
import com.example.library.model.book.Book;
import com.example.library.model.borrow.BorrowRecord;
import com.example.library.model.borrow.BorrowStatus;
import com.example.library.repository.book.BookRepository;
import com.example.library.repository.borrow.BorrowRepository;
import com.example.library.repository.user.UserRepository;
import com.example.library.service.borrow.BorrowService;
import com.example.library.service.validation.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

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

    @Override
    @Transactional(readOnly = true)
    public List<BorrowRecordDto> getOverdueRecordDtos() {
        List<BorrowRecord> records = borrowRepository.findByStatusAndDueDateBefore(BorrowStatus.BORROWED, LocalDate.now());
        return records.stream()
                .map(this::convertToDto)
                .toList();
    }

    private BorrowRecordDto convertToDto(BorrowRecord record) {
        BorrowRecordDto dto = new BorrowRecordDto();
        dto.setId(record.getId());
        dto.setBookTitle(record.getBook().getTitle());
        dto.setBookId(record.getBook().getId());
        dto.setBorrowerName(record.getUser().getFullName());
        dto.setBorrowerId(record.getUser().getId());
        dto.setBorrowDate(record.getBorrowDate());
        dto.setDueDate(record.getDueDate());
        dto.setReturnDate(record.getReturnDate());
        dto.setStatus(record.getStatus().name());

        // Tính fineAmount và daysLate
        if (record.getStatus() == BorrowStatus.RETURNED) {
            dto.setFineAmount(record.getFineAmount());
        } else if (record.getStatus() == BorrowStatus.BORROWED && record.getDueDate().isBefore(LocalDate.now())) {
            long daysLate = ChronoUnit.DAYS.between(record.getDueDate(), LocalDate.now());
            dto.setFineAmount(BigDecimal.valueOf(daysLate * ApplicationConstants.FINE_PER_DAY));
            dto.setDaysLate((int) daysLate);
        } else {
            dto.setFineAmount(BigDecimal.ZERO);
            dto.setDaysLate(0);
        }

        if (dto.getFineAmount().compareTo(BigDecimal.ZERO) > 0) {
            NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
            String formatted = numberFormat.format(dto.getFineAmount()) + " VNĐ";
            dto.setFormattedFineAmount(formatted);
        } else {
            dto.setFormattedFineAmount("");
        }

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowRecordDto> getHistoryDtosByUser(int userId) {
        return borrowRepository.findByUserIdOrderByBorrowDateDesc(userId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowRecordDto> getAllHistoryDtos() {
        return borrowRepository.findAllByOrderByBorrowDateDesc()
                .stream()
                .map(this::convertToDto)
                .toList();
    }
}