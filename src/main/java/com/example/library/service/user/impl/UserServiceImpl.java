package com.example.library.service.user.impl;

import com.example.library.model.user.Role;
import com.example.library.model.user.User;
import com.example.library.repository.user.UserRepository;
import com.example.library.service.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findByIsDeletedFalse();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public User createUser(User user) {
        // Luôn mã hóa mật khẩu khi tạo mới
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User registerReader(User user) {
        user.setRole(Role.READER);
        return register(user);
    }

    @Override
    public User updateUser(int id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng id=" + id));

        // Cập nhật thông tin cơ bản
        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setRole(updatedUser.getRole());

        // Chỉ mã hóa nếu mật khẩu mới được cung cấp (khác null và không rỗng)
        String newPassword = updatedUser.getPassword();
        if (newPassword != null && !newPassword.isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(newPassword));
        }

        return userRepository.save(existingUser);
    }

    @Override
    public void softDeleteById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng id=" + id));
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public User register(User user) {
        // Kiểm tra trùng username
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username đã tồn tại!");
        }
        // Đảm bảo role mặc định nếu chưa set
        if (user.getRole() == null) {
            user.setRole(Role.READER);
        }
        return createUser(user);
    }

    @Override
    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}