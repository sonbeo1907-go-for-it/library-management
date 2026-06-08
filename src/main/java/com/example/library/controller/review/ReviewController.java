package com.example.library.controller.review;

import com.example.library.constant.ApplicationConstants;
import com.example.library.constant.ScreenConstants;
import com.example.library.model.book.Book;
import com.example.library.model.review.Review;
import com.example.library.model.user.User;
import com.example.library.service.review.ReviewPageService;
import com.example.library.service.review.ReviewService;
import com.example.library.service.user.CurrentUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReviewController {

    private final ReviewPageService reviewPageService;
    private final CurrentUserService currentUserService;
    private final ReviewService reviewService; // thêm

    public ReviewController(ReviewPageService reviewPageService,
                            CurrentUserService currentUserService,
                            ReviewService reviewService) {
        this.reviewPageService = reviewPageService;
        this.currentUserService = currentUserService;
        this.reviewService = reviewService;
    }

    @GetMapping(ApplicationConstants.REVIEW_URL)
    public String showReviewForm(@RequestParam("bookId") int bookId, Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            Book book = reviewPageService.prepareReviewForm(bookId);
            model.addAttribute("book", book);

            User currentUser = currentUserService.getCurrentUser();
            if (currentUser != null) {
                Review existingReview = reviewService.getReviewByUserAndBook(currentUser.getId(), bookId);
                model.addAttribute("existingReview", existingReview);
            }
            return ScreenConstants.REVIEW_FORM;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" + ApplicationConstants.BOOK_DETAIL_URL.replace("{id}", String.valueOf(bookId));
        }
    }

    @PostMapping(ApplicationConstants.REVIEW_SAVE_URL)
    public String saveReview(@RequestParam("bookId") int bookId,
                             @RequestParam("rating") int rating,
                             @RequestParam("comment") String comment,
                             RedirectAttributes redirectAttributes) {
        try {
            reviewPageService.addOrUpdateReview(bookId, rating, comment);
            redirectAttributes.addFlashAttribute("successMessage", "Cảm ơn bạn đã đánh giá!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" + ApplicationConstants.REVIEW_URL + "?bookId=" + bookId;
        }
        return "redirect:" + ApplicationConstants.BOOK_DETAIL_URL.replace("{id}", String.valueOf(bookId));
    }
}