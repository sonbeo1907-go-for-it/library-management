package com.example.library.controller.review;

import com.example.library.constant.ApplicationConstants;
import com.example.library.constant.ScreenConstants;
import com.example.library.service.review.ReviewPageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReviewController {

    private final ReviewPageService reviewPageService;

    public ReviewController(ReviewPageService reviewPageService) {
        this.reviewPageService = reviewPageService;
    }

    @GetMapping(ApplicationConstants.REVIEW_URL)
    public String showReviewForm(@RequestParam("bookId") int bookId, Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("book", reviewPageService.prepareReviewForm(bookId));
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
            reviewPageService.addReview(bookId, rating, comment);
            redirectAttributes.addFlashAttribute("successMessage", "Cảm ơn bạn đã đánh giá sách!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" + ApplicationConstants.REVIEW_URL + "?bookId=" + bookId;
        }
        return "redirect:" + ApplicationConstants.BOOK_DETAIL_URL.replace("{id}", String.valueOf(bookId));
    }
}