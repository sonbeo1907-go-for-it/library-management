package com.example.library.service.cart;

import com.example.library.model.cart.Cart;
import com.example.library.model.cart.CartDto;
import com.example.library.model.cart.CartItem;
import com.example.library.model.book.Book;
import com.example.library.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartService {
    // Lấy giỏ hàng ACTIVE của user (nếu chưa có thì tạo mới)
    Cart getOrCreateActiveCart(User user);

    // Thêm sách vào giỏ
    CartItem addItem(int cartId, int bookId);

    // Xóa một item khỏi giỏ
    void removeItem(int cartItemId, User user);

    // Submit giỏ hàng (tạo mã code, chuyển sang SUBMITTED)
    CartDto submitCart(int userId);

    // Lấy CartDto cho reader
    CartDto getCartDto(int userId);

    // Lấy danh sách các giỏ SUBMITTED cho thủ thư/admin
    List<CartDto> getSubmittedCarts();

    // Tìm Cart theo id
    Cart findById(int cartId);

    // Duyệt giỏ hàng, tạo BorrowRecord, trừ sách, ghi nhận thủ thư duyệt
    void approveCart(int cartId, int approvedById);

    // Từ chối giỏ hàng
    void rejectCart(int cartId);

    List<CartDto> getCartsByUser(int userId);

    CartDto getCartDtoByCode(String cartCode);

    Page<CartDto> getSubmittedCarts(Pageable pageable);

    Page<CartDto> getCartsByUser(int userId, Pageable pageable);
}