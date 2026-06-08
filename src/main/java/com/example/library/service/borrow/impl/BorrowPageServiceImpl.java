package com.example.library.service.borrow.impl;

import com.example.library.constant.ApplicationConstants;
import com.example.library.constant.RoleConstants;
import com.example.library.model.book.Book;
import com.example.library.model.borrow.*;
import com.example.library.model.user.User;
import com.example.library.service.book.BookService;
import com.example.library.service.borrow.BorrowPageService;
import com.example.library.service.borrow.BorrowService;
import com.example.library.service.user.CurrentUserService;
import com.example.library.service.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class BorrowPageServiceImpl implements BorrowPageService {

    private final BorrowService borrowService;
    private final BookService bookService;
    private final UserService userService;
    private final CurrentUserService currentUserService;

    public BorrowPageServiceImpl(BorrowService borrowService, BookService bookService,
                                 UserService userService, CurrentUserService currentUserService) {
        this.borrowService = borrowService;
        this.bookService = bookService;
        this.userService = userService;
        this.currentUserService = currentUserService;
    }

    @Override
    public BorrowFormDto prepareBorrowForm(int bookId) {
        Book book = bookService.findById(bookId);
        if (book == null) {
            throw new RuntimeException("Không tìm thấy sách!");
        }
        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plusDays(ApplicationConstants.BORROW_DURATION_DAYS);
        List<User> users = Collections.emptyList();
        if (currentUserService.hasRole(RoleConstants.LIBRARIAN)) {
            users = userService.findAll();
        }
        return new BorrowFormDto(book, today, dueDate, users);
    }

    @Override
    public void processBorrow(int bookId, Integer userId) {
        if (userId == null) {
            User currentUser = currentUserService.getCurrentUser();
            if (currentUser == null) {
                throw new RuntimeException("Bạn cần đăng nhập để mượn sách.");
            }
            userId = currentUser.getId();
        }
        borrowService.borrowBook(bookId, userId);
    }

    @Override
    public ReturnConfirmDto prepareReturnConfirm(int recordId) {
        BorrowRecord record = borrowService.findById(recordId);
        if (record == null || record.getStatus() != BorrowStatus.BORROWED) {
            throw new RuntimeException("Bản ghi không hợp lệ hoặc sách đã được trả.");
        }
        LocalDate today = LocalDate.now();
        long daysLate = 0;
        BigDecimal fine = BigDecimal.ZERO;
        if (today.isAfter(record.getDueDate())) {
            daysLate = ChronoUnit.DAYS.between(record.getDueDate(), today);
            fine = BigDecimal.valueOf(daysLate * ApplicationConstants.FINE_PER_DAY);
        }
        String formattedFine = "";
        if (fine.compareTo(BigDecimal.ZERO) > 0) {
            NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
            formattedFine = numberFormat.format(fine) + " VNĐ";
        }

        ReturnConfirmDto dto = new ReturnConfirmDto();
        dto.setRecordId(record.getId());
        dto.setBorrowCode(record.getBorrowCode());           // khi thêm borrowCode sẽ có
        dto.setBookTitle(record.getBook().getTitle());
        dto.setBookAuthor(record.getBook().getAuthor());
        dto.setImageFilename(record.getBook().getImageFilename());
        dto.setBorrowerName(record.getUser().getFullName());
        dto.setBorrowDate(record.getBorrowDate());
        dto.setDueDate(record.getDueDate());
        dto.setToday(today);
        dto.setDaysLate(daysLate);
        dto.setFine(fine);
        dto.setFormattedFine(formattedFine);
        return dto;
    }

    @Override
    public void processReturn(int recordId) {
        borrowService.returnBook(recordId);
    }

    @Override
    public Page<BorrowRecordDto> getHistory(int page, int size, String status) {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("Bạn cần đăng nhập.");
        }
        Pageable pageable = PageRequest.of(page, size);
        if (currentUserService.hasRole(RoleConstants.LIBRARIAN) || currentUserService.hasRole(RoleConstants.ADMIN)) {
            return borrowService.getAllHistoryDtos(status, pageable);
        } else {
            return borrowService.getHistoryDtosByUser(currentUser.getId(), status, pageable);
        }
    }
    @Override
    public List<BorrowRecordDto> getOverdue() {
        return borrowService.getOverdueRecordDtos();
    }

    @Override
    public Page<BorrowRecordDto> getOverdue(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return borrowService.getOverdueRecordDtos(pageable);
    }
}