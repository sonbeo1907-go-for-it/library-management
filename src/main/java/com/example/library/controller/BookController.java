package com.example.library.controller;

import com.example.library.constant.ApplicationConstants;
import com.example.library.constant.ScreenConstants;
import com.example.library.model.Book;
import com.example.library.model.Review;
import com.example.library.model.User;
import com.example.library.service.BookService;
import com.example.library.service.CurrentUserService;
import com.example.library.service.ReviewService;
import com.example.library.service.ValidationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class BookController {

    private final BookService bookService;
    private final ReviewService reviewService;
    private final CurrentUserService currentUserService;
    private final ValidationService validationService;

    public BookController(BookService bookService, ReviewService reviewService, CurrentUserService currentUserService, ValidationService validationService) {
        this.bookService = bookService;
        this.reviewService = reviewService;
        this.currentUserService = currentUserService;
        this.validationService = validationService;
    }

    // Danh sách sách (có thể kèm tìm kiếm)
    @GetMapping(ApplicationConstants.BOOK_LIST_URL)
    public String listBooks(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("books", bookService.search(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("books", bookService.findAll());
        }
        return ScreenConstants.BOOK_LIST;
    }

    // Form thêm sách mới
    @GetMapping(ApplicationConstants.BOOK_NEW_URL)
    public String newBookForm(Model model) {
        model.addAttribute("book", new Book());
        return ScreenConstants.BOOK_FORM;
    }

    // Xử lý thêm sách
    @PostMapping(ApplicationConstants.BOOK_SAVE_URL)
    public String saveBook(@ModelAttribute Book book,
                           @RequestParam("imageFile") MultipartFile imageFile,
                           RedirectAttributes redirectAttributes) {
        try {
            bookService.save(book, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm sách thành công!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi upload ảnh: " + e.getMessage());
        }
        return "redirect:" + ApplicationConstants.BOOK_LIST_URL;
    }

    // Form sửa sách
    @GetMapping(ApplicationConstants.BOOK_EDIT_URL)
    public String editBookForm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        Book book = bookService.findById(id);
        if (book == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sách!");
            return "redirect:" + ApplicationConstants.BOOK_LIST_URL;
        }
        model.addAttribute("book", book);
        return ScreenConstants.BOOK_FORM;
    }

    // Xử lý cập nhật sách
    @PostMapping(ApplicationConstants.BOOK_EDIT_URL)
    public String updateBook(@PathVariable int id,
                             @ModelAttribute Book bookDetails,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             RedirectAttributes redirectAttributes) {
        try {
            Book updated = bookService.update(id, bookDetails, imageFile);
            if (updated == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sách để cập nhật!");
            } else {
                redirectAttributes.addFlashAttribute("successMessage", "Cập nhật sách thành công!");
            }
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi upload ảnh: " + e.getMessage());
        }
        return "redirect:" + ApplicationConstants.BOOK_LIST_URL;
    }

    // Xoá sách (soft delete)
    @GetMapping(ApplicationConstants.BOOK_DELETE_URL)
    public String deleteBook(@PathVariable int id, RedirectAttributes redirectAttributes) {
        bookService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xoá sách (soft delete).");
        return "redirect:" + ApplicationConstants.BOOK_LIST_URL;
    }

    // Xem chi tiết sách
    @GetMapping(ApplicationConstants.BOOK_DETAIL_URL)
    public String viewBookDetail(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        Book book = bookService.findById(id);
        if (book == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sách!");
            return "redirect:" + ApplicationConstants.BOOK_LIST_URL;
        }

        // Thông tin sách
        model.addAttribute("book", book);

        // Đánh giá
        List<Review> reviews = reviewService.getReviewsByBook(id);
        double averageRating = reviewService.getAverageRating(id);
        long reviewCount = reviewService.getReviewCount(id);

        model.addAttribute("reviews", reviews);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("reviewCount", reviewCount);

        // Kiểm tra người dùng hiện tại có thể đánh giá không (đã từng mượn và trả, chưa review)
        User currentUser = currentUserService.getCurrentUser();
        boolean canReview = false;
        if (currentUser != null) {
            try {
                validationService.validateCanAddReview(id, currentUser.getId());
                canReview = true;
            } catch (RuntimeException ignored) {
            }
        }
        model.addAttribute("canReview", canReview);

        return ScreenConstants.BOOK_DETAIL;
    }
}