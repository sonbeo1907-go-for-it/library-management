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
public class ApprovalController {

    private final CartPageService cartPageService;

    public ApprovalController(CartPageService cartPageService) {
        this.cartPageService = cartPageService;
    }

    @GetMapping(ApplicationConstants.APPROVAL_LIST_URL)
    public String listPendingCarts(@RequestParam(value = "code", required = false) String code,
                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                   Model model, RedirectAttributes redirectAttributes) {
        try {
            if (code != null && !code.isBlank()) {
                // Tìm kiếm theo mã giỏ - không phân trang
                CartDto cart = cartPageService.getCartByCode(code.trim());
                model.addAttribute("pendingCarts", List.of(cart));
                model.addAttribute("isSearchResult", true);
            } else {
                Page<CartDto> cartPage = cartPageService.getPendingCarts(page, size);
                model.addAttribute("page", cartPage);
                model.addAttribute("isSearchResult", false);
            }
            return ScreenConstants.APPROVAL_LIST;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" + ApplicationConstants.APPROVAL_LIST_URL;
        }
    }

    // Duyệt giỏ hàng
    @PostMapping(ApplicationConstants.APPROVAL_APPROVE_URL)
    public String approveCart(@PathVariable int cartId, RedirectAttributes redirectAttributes) {
        try {
            cartPageService.approveCart(cartId);
            redirectAttributes.addFlashAttribute("successMessage", "Đã duyệt giỏ hàng và cho mượn sách.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:" + ApplicationConstants.APPROVAL_LIST_URL;
    }

    // Từ chối giỏ hàng
    @PostMapping(ApplicationConstants.APPROVAL_REJECT_URL)
    public String rejectCart(@PathVariable int cartId, RedirectAttributes redirectAttributes) {
        try {
            cartPageService.rejectCart(cartId);
            redirectAttributes.addFlashAttribute("successMessage", "Đã từ chối giỏ hàng.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:" + ApplicationConstants.APPROVAL_LIST_URL;
    }
}