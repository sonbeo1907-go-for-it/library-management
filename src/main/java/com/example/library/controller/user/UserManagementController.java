package com.example.library.controller.user;

import com.example.library.constant.ApplicationConstants;
import com.example.library.constant.ScreenConstants;
import com.example.library.model.user.User;
import com.example.library.service.user.UserManagementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserManagementController {

    private final UserManagementService userManagementService;

    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @GetMapping(ApplicationConstants.USER_LIST_URL)
    public String listUsers(Model model) {
        model.addAttribute("users", userManagementService.getAllUsers());
        return ScreenConstants.USER_LIST;
    }

    @GetMapping(ApplicationConstants.USER_NEW_URL)
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        return ScreenConstants.USER_FORM;
    }

    @PostMapping(ApplicationConstants.USER_SAVE_URL)
    public String saveUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userManagementService.createUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Tạo người dùng thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:" + ApplicationConstants.USER_LIST_URL;
    }

    @GetMapping(ApplicationConstants.USER_EDIT_URL)
    public String editUserForm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("user", userManagementService.getUserById(id));
            return ScreenConstants.USER_FORM;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" + ApplicationConstants.USER_LIST_URL;
        }
    }

    @PostMapping(ApplicationConstants.USER_EDIT_URL)
    public String updateUser(@PathVariable int id, @ModelAttribute User user,
                             RedirectAttributes redirectAttributes) {
        try {
            userManagementService.updateUser(id, user);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật người dùng thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:" + ApplicationConstants.USER_LIST_URL;
    }

    @GetMapping(ApplicationConstants.USER_DELETE_URL)
    public String deleteUser(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            userManagementService.softDeleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa người dùng (soft delete).");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:" + ApplicationConstants.USER_LIST_URL;
    }
}