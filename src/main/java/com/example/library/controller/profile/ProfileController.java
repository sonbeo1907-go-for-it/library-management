package com.example.library.controller.profile;

import com.example.library.constant.ApplicationConstants;
import com.example.library.constant.ScreenConstants;
import com.example.library.service.profile.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping(ApplicationConstants.PROFILE_URL)
    public String viewProfile(Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("user", profileService.getCurrentUserProfile());
            return ScreenConstants.PROFILE;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" + ApplicationConstants.LOGIN_URL;
        }
    }

    @GetMapping(ApplicationConstants.PROFILE_EDIT_URL)
    public String editProfileForm(Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("user", profileService.getCurrentUserProfile());
            return ScreenConstants.PROFILE_EDIT;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" + ApplicationConstants.LOGIN_URL;
        }
    }

    @PostMapping(ApplicationConstants.PROFILE_EDIT_URL)
    public String updateProfile(@RequestParam("fullName") String fullName,
                                RedirectAttributes redirectAttributes) {
        try {
            profileService.updateFullName(fullName);
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
        try {
            profileService.changePassword(oldPassword, newPassword, confirmPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:" + ApplicationConstants.PROFILE_URL;
    }
}