package com.example.library.service;

import com.example.library.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    Optional<User> findByUsername(String username);
    Optional<User> findById(int id);
    User createUser(User user);       // luôn mã hóa mật khẩu
    User updateUser(int id, User user); // chỉ mã hóa nếu có password mới
    void softDeleteById(int id);      // soft delete
    User register(User user);
    boolean checkPassword(User user, String rawPassword);// gọi createUser
}