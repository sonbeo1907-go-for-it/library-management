package com.example.library.controller;

import com.example.library.constant.ApplicationConstants;
import com.example.library.constant.ScreenConstants;
import com.example.library.model.Book;
import com.example.library.model.User;
import com.example.library.service.BookService;
import com.example.library.service.CurrentUserService;
import com.example.library.service.ReviewService;
import com.example.library.service.ValidationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private final BookService bookService;
    private final CurrentUserService currentUserService;
    private final ValidationService validationService;

    public ReviewController(ReviewService reviewService,
                            BookService bookService,
                            CurrentUserService currentUserService,
                            ValidationService validationService) {
        this.reviewService = reviewService;
        this.bookService = bookService;
        this.currentUserService = currentUserService;
        this.validationService = validationService;
    }

    // Hiển thị form đánh giá sách
    @GetMapping(ApplicationConstants.REVIEW_URL)
    public String showReviewForm(@RequestParam("bookId") int bookId, Model model, RedirectAttributes redirectAttributes) {
        Book book = bookService.findById(bookId);
        if (book == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sách.");
            return "redirect:" + ApplicationConstants.BOOK_LIST_URL;
        }
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:" + ApplicationConstants.LOGIN_URL;
        }
        try {
            validationService.validateCanAddReview(bookId, currentUser.getId());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" + ApplicationConstants.BOOK_DETAIL_URL.replace("{id}", String.valueOf(bookId));
        }
        model.addAttribute("book", book);
        return ScreenConstants.REVIEW_FORM;
    }

    // Xử lý lưu đánh giá
    @PostMapping(ApplicationConstants.REVIEW_SAVE_URL)
    public String saveReview(@RequestParam("bookId") int bookId,
                             @RequestParam("rating") int rating,
                             @RequestParam("comment") String comment,
                             RedirectAttributes redirectAttributes) {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:" + ApplicationConstants.LOGIN_URL;
        }

        try {
            reviewService.addReview(bookId, currentUser.getId(), rating, comment);
            redirectAttributes.addFlashAttribute("successMessage", "Cảm ơn bạn đã đánh giá sách!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" + ApplicationConstants.REVIEW_URL + "?bookId=" + bookId;
        }

        return "redirect:" + ApplicationConstants.BOOK_DETAIL_URL.replace("{id}", String.valueOf(bookId));
    }
}