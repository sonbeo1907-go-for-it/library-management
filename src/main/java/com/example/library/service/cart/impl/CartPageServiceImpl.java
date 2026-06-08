package com.example.library.service.cart.impl;

import com.example.library.constant.RoleConstants;
import com.example.library.model.cart.Cart;
import com.example.library.model.cart.CartDto;
import com.example.library.model.user.User;
import com.example.library.service.cart.CartPageService;
import com.example.library.service.cart.CartService;
import com.example.library.service.user.CurrentUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartPageServiceImpl implements CartPageService {

    private final CartService cartService;
    private final CurrentUserService currentUserService;

    public CartPageServiceImpl(CartService cartService, CurrentUserService currentUserService) {
        this.cartService = cartService;
        this.currentUserService = currentUserService;
    }

    @Override
    public void addToCart(int bookId) {
        User user = currentUserService.getCurrentUser();
        if (user == null) throw new RuntimeException("Bạn cần đăng nhập.");
        Cart cart = cartService.getOrCreateActiveCart(user);
        cartService.addItem(cart.getId(), bookId);
    }

    @Override
    public void removeFromCart(int cartItemId) {
        User user = currentUserService.getCurrentUser();
        if (user == null) throw new RuntimeException("Bạn cần đăng nhập.");
        cartService.removeItem(cartItemId, user);
    }

    @Override
    public CartDto getCurrentCart() {
        User user = currentUserService.getCurrentUser();
        if (user == null) throw new RuntimeException("Bạn cần đăng nhập.");
        return cartService.getCartDto(user.getId());
    }

    @Override
    public CartDto getCartByCode(String cartCode) {
        if (!currentUserService.hasRole(RoleConstants.LIBRARIAN)
                && !currentUserService.hasRole(RoleConstants.ADMIN)) {
            throw new RuntimeException("Bạn không có quyền.");
        }
        return cartService.getCartDtoByCode(cartCode);
    }

    @Override
    public void submitCart() {
        User user = currentUserService.getCurrentUser();
        if (user == null) throw new RuntimeException("Bạn cần đăng nhập.");
        cartService.submitCart(user.getId());
    }

    @Override
    public Page<CartDto> getPendingCarts(int page, int size) {
        if (!currentUserService.hasRole(RoleConstants.LIBRARIAN)
                && !currentUserService.hasRole(RoleConstants.ADMIN)) {
            throw new RuntimeException("Bạn không có quyền.");
        }
        Pageable pageable = PageRequest.of(page, size);
        return cartService.getSubmittedCarts(pageable);
    }

    @Override
    public void approveCart(int cartId) {
        User approver = currentUserService.getCurrentUser();
        if (approver == null) throw new RuntimeException("Bạn cần đăng nhập.");
        if (!currentUserService.hasRole(RoleConstants.LIBRARIAN)
                && !currentUserService.hasRole(RoleConstants.ADMIN)) {
            throw new RuntimeException("Bạn không có quyền duyệt.");
        }
        cartService.approveCart(cartId, approver.getId());
    }

    @Override
    public void rejectCart(int cartId) {
        if (!currentUserService.hasRole(RoleConstants.LIBRARIAN)
                && !currentUserService.hasRole(RoleConstants.ADMIN)) {
            throw new RuntimeException("Bạn không có quyền.");
        }
        cartService.rejectCart(cartId);
    }

    @Override
    public Page<CartDto> getMyCarts(int page, int size) {
        User user = currentUserService.getCurrentUser();
        if (user == null) throw new RuntimeException("Bạn cần đăng nhập.");
        Pageable pageable = PageRequest.of(page, size);
        return cartService.getCartsByUser(user.getId(), pageable);
    }
}