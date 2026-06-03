package com.example.library.controller;

import com.example.library.constant.ApplicationConstants;
import com.example.library.constant.ScreenConstants;
import com.example.library.model.User;
import com.example.library.service.CurrentUserService;
import com.example.library.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final CurrentUserService currentUserService;
    private final UserService userService;

    public ProfileController(CurrentUserService currentUserService, UserService userService) {
        this.currentUserService = currentUserService;
        this.userService = userService;
    }

    @GetMapping(ApplicationConstants.PROFILE_URL)
    public String viewProfile(Model model) {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:" + ApplicationConstants.LOGIN_URL;
        }
        model.addAttribute("user", currentUser);
        return ScreenConstants.PROFILE;
    }

    @GetMapping(ApplicationConstants.PROFILE_EDIT_URL)
    public String editProfileForm(Model model) {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:" + ApplicationConstants.LOGIN_URL;
        }
        model.addAttribute("user", currentUser);
        return ScreenConstants.PROFILE_EDIT;
    }

    @PostMapping(ApplicationConstants.PROFILE_EDIT_URL)
    public String updateProfile(@RequestParam("fullName") String fullName,
                                RedirectAttributes redirectAttributes) {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:" + ApplicationConstants.LOGIN_URL;
        }
        try {
            User updatedUser = new User();
            updatedUser.setFullName(fullName);
            updatedUser.setRole(currentUser.getRole());
            updatedUser.setPassword(null);
            userService.updateUser(currentUser.getId(), updatedUser);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:" + ApplicationConstants.PROFILE_URL;
    }

    @GetMapping(ApplicationConstants.CHANGE_PASSWORD_URL)
    public String changePasswordForm() {
        return ScreenConstants.CHANGE_PASSWORD;
    }

    @PostMapping(ApplicationConstants.CHANGE_PASSWORD_URL)
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 RedirectAttributes redirectAttributes) {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:" + ApplicationConstants.LOGIN_URL;
        }

        if (!userService.checkPassword(currentUser, oldPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu cũ không đúng.");
            return "redirect:" + ApplicationConstants.CHANGE_PASSWORD_URL;
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu mới không khớp.");
            return "redirect:" + ApplicationConstants.CHANGE_PASSWORD_URL;
        }

        try {
            User updateUser = new User();
            updateUser.setPassword(newPassword);
            updateUser.setRole(currentUser.getRole());
            updateUser.setFullName(currentUser.getFullName());
            userService.updateUser(currentUser.getId(), updateUser);
            redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:" + ApplicationConstants.PROFILE_URL;
    }
}