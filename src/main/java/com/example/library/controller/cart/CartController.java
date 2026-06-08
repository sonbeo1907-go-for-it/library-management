package com.example.library.controller.cart;

import com.example.library.constant.ApplicationConstants;
import com.example.library.constant.ScreenConstants;
import com.example.library.model.cart.CartDto;
import com.example.library.service.cart.CartPageService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CartController {

    private final CartPageService cartPageService;

    public CartController(CartPageService cartPageService) {
        this.cartPageService = cartPageService;
    }

    // Hiển thị giỏ hàng
    @GetMapping(ApplicationConstants.CART_URL)
    public String viewCart(Model model, RedirectAttributes redirectAttributes) {
        try {
            var cartDto = cartPageService.getCurrentCart();
            model.addAttribute("cart", cartDto);
            return ScreenConstants.CART;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" + ApplicationConstants.HOME_URL;
        }
    }

    // Thêm sách vào giỏ (từ book detail hoặc book list)
    @PostMapping(ApplicationConstants.CART_ADD_URL)
    public String addToCart(@PathVariable int bookId, RedirectAttributes redirectAttributes) {
        try {
            cartPageService.addToCart(bookId);
            redirectAttributes.addFlashAttribute("successMessage", "Đã thêm sách vào giỏ!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:" + ApplicationConstants.BOOK_LIST_URL;
    }

    // Xóa một item khỏi giỏ
    @PostMapping(ApplicationConstants.CART_REMOVE_URL)
    public String removeFromCart(@PathVariable int cartItemId, RedirectAttributes redirectAttributes) {
        try {
            cartPageService.removeFromCart(cartItemId);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa khỏi giỏ.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:" + ApplicationConstants.CART_URL;
    }

    // Gửi giỏ hàng chờ duyệt
    @PostMapping(ApplicationConstants.CART_SUBMIT_URL)
    public String submitCart(RedirectAttributes redirectAttributes) {
        try {
            cartPageService.submitCart();
            redirectAttributes.addFlashAttribute("successMessage", "Đã gửi giỏ hàng chờ duyệt. Vui lòng đến quầy thủ thư để xác nhận.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:" + ApplicationConstants.CART_URL;
    }

    @GetMapping(ApplicationConstants.MY_CARTS_URL)
    public String myCarts(Model model,
                          @RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "size", defaultValue = "10") int size,
                          RedirectAttributes redirectAttributes) {
        try {
            Page<CartDto> cartPage = cartPageService.getMyCarts(page, size);
            model.addAttribute("page", cartPage);
            return ScreenConstants.MY_CARTS;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" + ApplicationConstants.HOME_URL;
        }
    }
}