package com.example.library.controller.admin;

import com.example.library.constant.ApplicationConstants;
import com.example.library.constant.ScreenConstants;
import com.example.library.model.revenue.RevenueSummaryDto;
import com.example.library.service.revenue.RevenueService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class RevenueController {

    private final RevenueService revenueService;

    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @GetMapping(ApplicationConstants.ADMIN_REVENUE_URL)
    public String viewRevenue(@RequestParam(value = "year", required = false) Integer year,
                              @RequestParam(value = "month", required = false) Integer month,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size,
                              Model model) {
        if (year == null) year = LocalDate.now().getYear();
        if (month == null) month = LocalDate.now().getMonthValue();

        PageRequest pageable = PageRequest.of(page, size);
        var revenuePage = revenueService.getRevenueByMonth(year, month, pageable);
        RevenueSummaryDto summary = revenueService.getRevenueSummary(year, month);
        model.addAttribute("summary", summary);
        model.addAttribute("page", revenuePage);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        return ScreenConstants.REVENUE;
    }
}