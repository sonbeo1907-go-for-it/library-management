package com.example.library.model.cart;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CartDto {
    private int cartId;
    private String cartCode;
    private CartStatus status;
    private List<CartItemDto> items;
    private BigDecimal totalFee;
    private LocalDateTime createdAt;
    private LocalDateTime submittedAt;
    private String formattedTotalFee;

    // Thông tin người dùng (để hiển thị ở màn duyệt)
    private String username;
    private String fullName;

    public CartDto() {}

    // Getters and Setters
    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }
    public String getCartCode() { return cartCode; }
    public void setCartCode(String cartCode) { this.cartCode = cartCode; }
    public CartStatus getStatus() { return status; }
    public void setStatus(CartStatus status) { this.status = status; }
    public List<CartItemDto> getItems() { return items; }
    public void setItems(List<CartItemDto> items) { this.items = items; }
    public BigDecimal getTotalFee() { return totalFee; }
    public void setTotalFee(BigDecimal totalFee) { this.totalFee = totalFee; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getFormattedTotalFee() {
        return formattedTotalFee;
    }

    public void setFormattedTotalFee(String formattedTotalFee) {
        this.formattedTotalFee = formattedTotalFee;
    }
}