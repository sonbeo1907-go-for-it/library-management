package com.example.library.controller;

import com.example.library.constant.ApplicationConstants;
import com.example.library.constant.RoleConstants;
import com.example.library.constant.ScreenConstants;
import com.example.library.model.BorrowRecord;
import com.example.library.model.BorrowStatus;
import com.example.library.model.Role;
import com.example.library.model.User;
import com.example.library.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class AuthController {

    private final UserService userService;
    private final CurrentUserService currentUserService;
    private final BookStatisticsService statisticsService;
    private final BorrowService borrowService;

    public AuthController(UserService userService,
                          CurrentUserService currentUserService,
                          BookStatisticsService statisticsService,
                          BorrowService borrowService) {
        this.userService = userService;
        this.currentUserService = currentUserService;
        this.statisticsService = statisticsService;
        this.borrowService = borrowService;
    }

    @GetMapping(ApplicationConstants.LOGIN_URL)
    public String loginPage(Model model,
                            @RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout) {
        if (error != null) {
            model.addAttribute("errorMessage", "Sai tên đăng nhập hoặc mật khẩu");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Bạn đã đăng xuất thành công");
        }
        return ScreenConstants.LOGIN;
    }

    @GetMapping(ApplicationConstants.REGISTER_URL)
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return ScreenConstants.REGISTER;
    }

    @PostMapping(ApplicationConstants.REGISTER_URL)
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            user.setRole(Role.READER);
            userService.register(user);
            return "redirect:" + ApplicationConstants.LOGIN_URL + "?registered=true";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return ScreenConstants.REGISTER;
        }
    }

    @GetMapping(ApplicationConstants.HOME_URL)
    public String home(Model model) {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:" + ApplicationConstants.LOGIN_URL;
        }

        model.addAttribute("user", currentUser);
        if (currentUserService.hasRole(RoleConstants.LIBRARIAN)) {
            model.addAttribute("totalBooks", statisticsService.countTotalBooks());
            model.addAttribute("availableBooks", statisticsService.countAvailableBooks());
            model.addAttribute("borrowedBooks", statisticsService.countBorrowedBooks());
            model.addAttribute("overdueBooks", statisticsService.countOverdueBooks());
            model.addAttribute("totalUsers", statisticsService.countTotalUsers());
        } else {
            List<BorrowRecord> history = borrowService.getHistoryByUser(currentUser.getId());
            long currentlyBorrowed = history.stream()
                    .filter(r -> r.getStatus() == BorrowStatus.BORROWED)
                    .count();
            long overdue = history.stream()
                    .filter(r -> r.getStatus() == BorrowStatus.BORROWED
                            && r.getDueDate().isBefore(LocalDate.now()))
                    .count();
            model.addAttribute("currentlyBorrowed", currentlyBorrowed);
            model.addAttribute("overdue", overdue);
        }
        return ScreenConstants.HOME;
    }
}