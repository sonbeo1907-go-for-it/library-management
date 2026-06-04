package com.example.library.controller.borrow;

import com.example.library.constant.ApplicationConstants;
import com.example.library.constant.ScreenConstants;
import com.example.library.service.borrow.BorrowPageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BorrowController {

    private final BorrowPageService borrowPageService;

    public BorrowController(BorrowPageService borrowPageService) {
        this.borrowPageService = borrowPageService;
    }

    @GetMapping(ApplicationConstants.BORROW_URL)
    public String borrowForm(@RequestParam("bookId") int bookId, Model model, RedirectAttributes redirectAttributes) {
        try {
            var formDto = borrowPageService.prepareBorrowForm(bookId);
            model.addAttribute("book", formDto.getBook());
            model.addAttribute("borrowDate", formDto.getBorrowDate());
            model.addAttribute("dueDate", formDto.getDueDate());
            model.addAttribute("users", formDto.getUsers());
            return ScreenConstants.BORROW_FORM;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" + ApplicationConstants.BOOK_LIST_URL;
        }
    }

    @PostMapping(ApplicationConstants.BORROW_URL)
    public String borrowBook(@RequestParam("bookId") int bookId,
                             @RequestParam(value = "userId", required = false) Integer userId,
                             RedirectAttributes redirectAttributes) {
        try {
            borrowPageService.processBorrow(bookId, userId);
            redirectAttributes.addFlashAttribute("successMessage", "Mượn sách thành công!");
            return "redirect:" + ApplicationConstants.HISTORY_URL;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" + ApplicationConstants.BORROW_URL + "?bookId=" + bookId;
        }
    }

    @GetMapping(ApplicationConstants.RETURN_URL)
    public String returnConfirm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            var confirmDto = borrowPageService.prepareReturnConfirm(id);
            model.addAttribute("record", confirmDto.getRecord());
            model.addAttribute("today", confirmDto.getToday());
            model.addAttribute("daysLate", confirmDto.getDaysLate());
            model.addAttribute("fine", confirmDto.getFine());
            model.addAttribute("formattedFine", confirmDto.getFormattedFine());
            return ScreenConstants.RETURN_CONFIRM;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" + ApplicationConstants.HISTORY_URL;
        }
    }

    @PostMapping(ApplicationConstants.RETURN_URL)
    public String returnBook(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            borrowPageService.processReturn(id);
            redirectAttributes.addFlashAttribute("successMessage", "Trả sách thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:" + ApplicationConstants.HISTORY_URL;
    }

    @GetMapping(ApplicationConstants.HISTORY_URL)
    public String history(Model model) {
        model.addAttribute("records", borrowPageService.getHistory());
        return ScreenConstants.HISTORY;
    }

    @GetMapping(ApplicationConstants.OVERDUE_URL)
    public String overdue(Model model) {
        model.addAttribute("records", borrowPageService.getOverdue());
        return ScreenConstants.OVERDUE;
    }
}