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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
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
        return new ReturnConfirmDto(record, today, daysLate, fine, formattedFine);
    }

    @Override
    public void processReturn(int recordId) {
        borrowService.returnBook(recordId);
    }

    @Override
    public List<BorrowRecordDto> getHistory() {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("Bạn cần đăng nhập.");
        }
        if (currentUserService.hasRole(RoleConstants.LIBRARIAN)) {
            return borrowService.getAllHistoryDtos();
        } else {
            return borrowService.getHistoryDtosByUser(currentUser.getId());
        }
    }

    @Override
    public List<BorrowRecordDto> getOverdue() {
        return borrowService.getOverdueRecordDtos();
    }
}