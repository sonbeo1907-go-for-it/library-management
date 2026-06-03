package com.example.library.controller;

import com.example.library.constant.ApplicationConstants;
import com.example.library.constant.ScreenConstants;
import com.example.library.model.User;
import com.example.library.service.UserService;
import com.example.library.service.ValidationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserManagementController {

    private final UserService userService;
    private final ValidationService validationService;

    public UserManagementController(UserService userService, ValidationService validationService) {
        this.userService = userService;
        this.validationService = validationService;
    }

    // Danh sách người dùng
    @GetMapping(ApplicationConstants.USER_LIST_URL)
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return ScreenConstants.USER_LIST;
    }

    // Form thêm người dùng mới
    @GetMapping(ApplicationConstants.USER_NEW_URL)
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        return ScreenConstants.USER_FORM;
    }

    // Xử lý thêm người dùng
    @PostMapping(ApplicationConstants.USER_SAVE_URL)
    public String saveUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.createUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Tạo người dùng thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:" + ApplicationConstants.USER_LIST_URL;
    }

    // Form sửa người dùng
    @GetMapping(ApplicationConstants.USER_EDIT_URL)
    public String editUserForm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        var userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy người dùng!");
            return "redirect:" + ApplicationConstants.USER_LIST_URL;
        }
        model.addAttribute("user", userOpt.get());
        return ScreenConstants.USER_FORM;
    }

    @PostMapping(ApplicationConstants.USER_EDIT_URL)
    public String updateUser(@PathVariable int id, @ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(id, user);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật người dùng thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:" + ApplicationConstants.USER_LIST_URL;
    }

    // Xóa người dùng (soft delete)
    @GetMapping(ApplicationConstants.USER_DELETE_URL)
    public String deleteUser(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            userService.softDeleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa người dùng (soft delete).");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:" + ApplicationConstants.USER_LIST_URL;
    }
}