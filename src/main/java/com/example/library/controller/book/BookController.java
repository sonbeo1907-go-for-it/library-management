package com.example.library.controller.book;

import com.example.library.constant.ApplicationConstants;
import com.example.library.constant.ScreenConstants;
import com.example.library.model.book.Book;
import com.example.library.service.book.BookPageService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class BookController {

    private final BookPageService bookPageService;

    public BookController(BookPageService bookPageService) {
        this.bookPageService = bookPageService;
    }

    @GetMapping(ApplicationConstants.BOOK_LIST_URL)
    public String listBooks(Model model,
                            @RequestParam(value = "keyword", required = false) String keyword,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "12") int size) {
        Page<Book> bookPage = bookPageService.getBooksForList(keyword, page, size);
        model.addAttribute("page", bookPage);
        model.addAttribute("keyword", keyword);
        return ScreenConstants.BOOK_LIST;
    }

    @GetMapping(ApplicationConstants.BOOK_NEW_URL)
    public String newBookForm(Model model) {
        model.addAttribute("book", new Book());
        return ScreenConstants.BOOK_FORM;
    }

    @PostMapping(ApplicationConstants.BOOK_SAVE_URL)
    public String saveBook(@ModelAttribute Book book,
                           @RequestParam("imageFile") MultipartFile imageFile,
                           RedirectAttributes redirectAttributes) {
        try {
            bookPageService.saveBook(book, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm sách thành công!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi upload ảnh: " + e.getMessage());
        }
        return "redirect:" + ApplicationConstants.BOOK_LIST_URL;
    }

    @GetMapping(ApplicationConstants.BOOK_EDIT_URL)
    public String editBookForm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        Book book = bookPageService.getBooksForList(null).stream()
                .filter(b -> b.getId() == id).findFirst().orElse(null);
        if (book == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sách!");
            return "redirect:" + ApplicationConstants.BOOK_LIST_URL;
        }
        model.addAttribute("book", book);
        return ScreenConstants.BOOK_FORM;
    }

    @PostMapping(ApplicationConstants.BOOK_EDIT_URL)
    public String updateBook(@PathVariable int id,
                             @ModelAttribute Book bookDetails,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             RedirectAttributes redirectAttributes) {
        try {
            Book updated = bookPageService.updateBook(id, bookDetails, imageFile);
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

    @GetMapping(ApplicationConstants.BOOK_DELETE_URL)
    public String deleteBook(@PathVariable int id, RedirectAttributes redirectAttributes) {
        bookPageService.deleteBook(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xoá sách (soft delete).");
        return "redirect:" + ApplicationConstants.BOOK_LIST_URL;
    }

    @GetMapping(ApplicationConstants.BOOK_DETAIL_URL)
    public String viewBookDetail(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        var detail = bookPageService.getBookDetail(id);
        if (detail == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sách!");
            return "redirect:" + ApplicationConstants.BOOK_LIST_URL;
        }
        model.addAttribute("book", detail.getBook());
        model.addAttribute("reviews", detail.getReviews());
        model.addAttribute("averageRating", detail.getAverageRating());
        model.addAttribute("reviewCount", detail.getReviewCount());
        model.addAttribute("canReview", detail.isCanReview());
        return ScreenConstants.BOOK_DETAIL;
    }
}