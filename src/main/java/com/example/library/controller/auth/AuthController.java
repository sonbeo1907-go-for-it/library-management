package com.example.library.controller.auth;

import com.example.library.constant.ApplicationConstants;
import com.example.library.constant.ScreenConstants;
import com.example.library.model.user.Role;
import com.example.library.model.user.User;
import com.example.library.service.book.BookService;
import com.example.library.service.home.HomeService;
import com.example.library.service.user.CurrentUserService;
import com.example.library.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;
    private final CurrentUserService currentUserService;
    private final HomeService homeService;
    private final BookService bookService;

    public AuthController(UserService userService,
                          CurrentUserService currentUserService,
                          HomeService homeService, BookService bookService) {
        this.userService = userService;
        this.currentUserService = currentUserService;
        this.homeService = homeService;
        this.bookService = bookService;
    }

    @GetMapping(ApplicationConstants.LOGIN_URL)
    public String loginPage(Model model,
                            @RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout) {
        if (error != null) model.addAttribute("errorMessage", "Sai tên đăng nhập hoặc mật khẩu");
        if (logout != null) model.addAttribute("logoutMessage", "Bạn đã đăng xuất thành công");
        return ScreenConstants.LOGIN;
    }

    @GetMapping(ApplicationConstants.REGISTER_URL)
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return ScreenConstants.REGISTER;
    }

    @PostMapping(ApplicationConstants.REGISTER_URL)
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.registerReader(user);
            redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" + ApplicationConstants.REGISTER_URL;
        }
        return "redirect:" + ApplicationConstants.LOGIN_URL + "?registered=true";
    }

    @GetMapping(ApplicationConstants.HOME_URL)
    public String home(Model model) {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:" + ApplicationConstants.LOGIN_URL;
        }
        model.addAttribute("homeDto", homeService.getHomeData(currentUser));

        // Gợi ý sách ngẫu nhiên cho độc giả
        if (currentUser.getRole() == Role.READER) {
            model.addAttribute("randomBooks", bookService.getRandomBooks(6));
        }
        return ScreenConstants.HOME;
    }
}