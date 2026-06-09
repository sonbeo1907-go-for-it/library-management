package com.example.library.controller.admin;

import com.example.library.constant.ApplicationConstants;
import com.example.library.constant.ScreenConstants;
import com.example.library.model.salary.*;
import com.example.library.model.user.Role;
import com.example.library.model.user.User;
import com.example.library.service.salary.SalaryService;
import com.example.library.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class SalaryController {

    private final SalaryService salaryService;
    private final UserService userService;

    public SalaryController(SalaryService salaryService, UserService userService) {
        this.salaryService = salaryService;
        this.userService = userService;
    }

    @GetMapping(ApplicationConstants.ADMIN_SALARIES_URL)
    public String listSalaries(@RequestParam(value = "year", required = false) Integer year,
                               @RequestParam(value = "month", required = false) Integer month,
                               @RequestParam(value = "search", required = false) String search,
                               Model model) {
        if (year == null) year = LocalDate.now().getYear();
        if (month == null) month = LocalDate.now().getMonthValue();

        List<LibrarianSalaryDto> librarians = salaryService.getLibrarianSalaries(year, month, search);
        model.addAttribute("librarians", librarians);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("search", search);
        return ScreenConstants.ADMIN_SALARIES;
    }

    @PostMapping(ApplicationConstants.ADMIN_SALARIES_CALCULATE_URL)
    public String calculateSalary(@RequestParam int year, @RequestParam int month,
                                  RedirectAttributes redirectAttributes) {
        try {
            salaryService.calculateMonthlySalary(year, month);
            redirectAttributes.addFlashAttribute("successMessage", "Đã tính lương tháng " + month + "/" + year);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:" + ApplicationConstants.ADMIN_SALARIES_URL + "?year=" + year + "&month=" + month;
    }

    @PostMapping(ApplicationConstants.ADMIN_SALARIES_PAY_URL)
    public String paySalary(@PathVariable int id, @RequestParam int year, @RequestParam int month,
                            RedirectAttributes redirectAttributes) {
        try {
            salaryService.paySalary(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã thanh toán lương.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:" + ApplicationConstants.ADMIN_SALARIES_URL + "?year=" + year + "&month=" + month;
    }

    @PostMapping(ApplicationConstants.ADMIN_SALARIES_CANCEL_URL)
    public String cancelSalary(@PathVariable int id, @RequestParam int year, @RequestParam int month,
                               RedirectAttributes redirectAttributes) {
        try {
            salaryService.cancelSalary(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã hủy lương.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:" + ApplicationConstants.ADMIN_SALARIES_URL + "?year=" + year + "&month=" + month;
    }

    @GetMapping(ApplicationConstants.ADMIN_SALARIES_CONFIG_URL)
    public String configSalaryForm(@PathVariable int userId, Model model,
                                   RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            if (user.getRole() != Role.LIBRARIAN) {
                redirectAttributes.addFlashAttribute("errorMessage", "Chỉ có thể cấu hình lương cho thủ thư.");
                return "redirect:" + ApplicationConstants.ADMIN_SALARIES_URL;
            }
            SalaryConfigDto configDto = salaryService.getSalaryConfigDto(userId);
            model.addAttribute("config", configDto);
            return ScreenConstants.ADMIN_SALARY_CONFIG;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" + ApplicationConstants.ADMIN_SALARIES_URL;
        }
    }

    @PostMapping(ApplicationConstants.ADMIN_SALARIES_CONFIG_URL)
    public String saveSalaryConfig(@PathVariable int userId, @ModelAttribute SalaryConfigDto configDto,
                                   RedirectAttributes redirectAttributes) {
        try {
            configDto.setUserId(userId);
            salaryService.saveSalaryConfigDto(configDto);
            redirectAttributes.addFlashAttribute("successMessage", "Đã lưu cấu hình lương.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:" + ApplicationConstants.ADMIN_SALARIES_URL;
    }
}