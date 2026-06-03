package com.example.library.controller;

import com.example.library.constant.ApplicationConstants;
import com.example.library.constant.ScreenConstants;
import com.example.library.service.BookStatisticsService;
import com.example.library.service.CurrentUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final BookStatisticsService statisticsService;
    private final CurrentUserService currentUserService;

    public DashboardController(BookStatisticsService statisticsService,
                               CurrentUserService currentUserService) {
        this.statisticsService = statisticsService;
        this.currentUserService = currentUserService;
    }

    @GetMapping(ApplicationConstants.DASHBOARD_URL)
    public String dashboard(Model model) {
        // Chỉ cho phép Thủ thư (LIBRARIAN) truy cập – nếu chưa có Security URL level
        if (!currentUserService.hasRole("ROLE_LIBRARIAN")) {
            return "redirect:" + ApplicationConstants.HOME_URL;
        }

        model.addAttribute("totalBooks", statisticsService.countTotalBooks());
        model.addAttribute("availableBooks", statisticsService.countAvailableBooks());
        model.addAttribute("borrowedBooks", statisticsService.countBorrowedBooks());
        model.addAttribute("overdueBooks", statisticsService.countOverdueBooks());
        model.addAttribute("totalUsers", statisticsService.countTotalUsers());
        model.addAttribute("activeBorrowings", statisticsService.countActiveBorrowings());

        return ScreenConstants.DASHBOARD;
    }
}