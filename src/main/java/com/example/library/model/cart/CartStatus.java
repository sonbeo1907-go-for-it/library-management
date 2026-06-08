package com.example.library.model.cart;

public enum CartStatus {
    ACTIVE,      // Đang thêm sách
    SUBMITTED,   // Đã gửi chờ duyệt
    APPROVED,    // Đã được thủ thư duyệt
    REJECTED     // Bị từ chối
}