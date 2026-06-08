package com.example.library.service.cart;

import com.example.library.model.cart.CartDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CartPageService {
    // Thêm sách vào giỏ hàng của user hiện tại
    void addToCart(int bookId);

    // Xóa item khỏi giỏ
    void removeFromCart(int cartItemId);

    // Lấy giỏ hàng hiện tại của user
    CartDto getCurrentCart();

    // Gửi giỏ hàng chờ duyệt
    void submitCart();

    // Lấy danh sách giỏ chờ duyệt (cho thủ thư/admin)
    Page<CartDto> getPendingCarts(int page, int size);

    // Duyệt giỏ hàng
    void approveCart(int cartId);

    // Từ chối giỏ hàng
    void rejectCart(int cartId);

    CartDto getCartByCode(String cartCode);

    Page<CartDto> getMyCarts(int page, int size);
}