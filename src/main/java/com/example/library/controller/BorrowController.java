package com.example.library.controller;

import com.example.library.constant.ApplicationConstants;
import com.example.library.constant.RoleConstants;
import com.example.library.constant.ScreenConstants;
import com.example.library.model.BorrowRecord;
import com.example.library.model.BorrowStatus;
import com.example.library.model.User;
import com.example.library.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BorrowController {

    private final BorrowService borrowService;
    private final BookService bookService;
    private final UserService userService;
    private final CurrentUserService currentUserService;
    private final ReviewService reviewService;

    public BorrowController(BorrowService borrowService,
                            BookService bookService,
                            UserService userService,
                            CurrentUserService currentUserService,
                            ReviewService reviewService) {
        this.borrowService = borrowService;
        this.bookService = bookService;
        this.userService = userService;
        this.currentUserService = currentUserService;
        this.reviewService = reviewService;
    }

    // Hiển thị form mượn sách (cho cả thủ thư và độc giả)
    @GetMapping(ApplicationConstants.BORROW_URL)
    public String borrowForm(@RequestParam("bookId") int bookId, Model model,
                             RedirectAttributes redirectAttributes) {
        var book = bookService.findById(bookId);
        if (book == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sách!");
            return "redirect:" + ApplicationConstants.BOOK_LIST_URL;
        }
        model.addAttribute("book", book);

        if (currentUserService.hasRole("ROLE_LIBRARIAN")) {
            model.addAttribute("users", userService.findAll());
        }
        return ScreenConstants.BORROW_FORM;
    }

    @PostMapping(ApplicationConstants.BORROW_URL)
    public String borrowBook(@RequestParam("bookId") int bookId,
                             @RequestParam(value = "userId", required = false) Integer userId,
                             RedirectAttributes redirectAttributes) {
        try {
            if (userId == null) {
                User currentUser = currentUserService.getCurrentUser();
                if (currentUser == null) {
                    throw new RuntimeException("Bạn cần đăng nhập để mượn sách.");
                }
                userId = currentUser.getId();
            }
            borrowService.borrowBook(bookId, userId);
            redirectAttributes.addFlashAttribute("successMessage", "Mượn sách thành công!");
            return "redirect:" + ApplicationConstants.HISTORY_URL;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" + ApplicationConstants.BORROW_URL + "?bookId=" + bookId;
        }
    }

    @GetMapping(ApplicationConstants.RETURN_URL)
    public String returnConfirm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        BorrowRecord record = borrowService.findById(id);
        if (record == null || record.getStatus() != BorrowStatus.BORROWED) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bản ghi không hợp lệ hoặc sách đã được trả.");
            return "redirect:" + ApplicationConstants.HISTORY_URL;
        }

        LocalDate today = LocalDate.now();
        long daysLate = 0;
        BigDecimal fine = BigDecimal.ZERO;
        if (today.isAfter(record.getDueDate())) {
            daysLate = ChronoUnit.DAYS.between(record.getDueDate(), today);
            fine = BigDecimal.valueOf(daysLate * ApplicationConstants.FINE_PER_DAY);
        }

        model.addAttribute("record", record);
        model.addAttribute("today", today);
        model.addAttribute("daysLate", daysLate);
        model.addAttribute("fine", fine);
        return ScreenConstants.RETURN_CONFIRM;
    }

    @PostMapping(ApplicationConstants.RETURN_URL)
    public String returnBook(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            BorrowRecord record = borrowService.returnBook(id);
            String message = "Trả sách thành công!";
            if (record.getFineAmount().compareTo(BigDecimal.ZERO) > 0) {
                message += " Phí quá hạn: " + record.getFineAmount() + " VNĐ.";
            }
            redirectAttributes.addFlashAttribute("successMessage", message);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:" + ApplicationConstants.HISTORY_URL;
    }


    @GetMapping(ApplicationConstants.HISTORY_URL)
    public String history(Model model) {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:" + ApplicationConstants.LOGIN_URL;
        }

        List<BorrowRecord> records;
        if (currentUserService.hasRole(RoleConstants.LIBRARIAN)) {
            records = borrowService.getAllHistory();
        } else {
            records = borrowService.getHistoryByUser(currentUser.getId());
        }

        // Tạo map reviewStatus: bookId -> đã review chưa (chỉ cho người dùng hiện tại)
        Map<Integer, Boolean> reviewStatusMap = new HashMap<>();
        for (BorrowRecord record : records) {
            if (record.getUser().getId() == currentUser.getId()) { // chỉ kiểm tra nếu người mượn là current user
                boolean hasReviewed = reviewService.hasUserReviewedBook(currentUser.getId(), record.getBook().getId());
                reviewStatusMap.put(record.getBook().getId(), hasReviewed);
            }
        }

        model.addAttribute("records", records);
        model.addAttribute("reviewStatusMap", reviewStatusMap);
        return ScreenConstants.HISTORY;
    }

    // Danh sách quá hạn (chỉ thủ thư)
    @GetMapping(ApplicationConstants.OVERDUE_URL)
    public String overdue(Model model) {
        model.addAttribute("records", borrowService.getOverdueRecords());
        return ScreenConstants.OVERDUE;
    }


}