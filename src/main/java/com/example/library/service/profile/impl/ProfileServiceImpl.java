package com.example.library.service.profile.impl;

import com.example.library.model.user.User;
import com.example.library.service.profile.ProfileService;
import com.example.library.service.user.CurrentUserService;
import com.example.library.service.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final CurrentUserService currentUserService;
    private final UserService userService;

    public ProfileServiceImpl(CurrentUserService currentUserService, UserService userService) {
        this.currentUserService = currentUserService;
        this.userService = userService;
    }

    @Override
    public User getCurrentUserProfile() {
        User user = currentUserService.getCurrentUser();
        if (user == null) {
            throw new RuntimeException("Bạn cần đăng nhập.");
        }
        return user;
    }

    @Override
    public void updateFullName(String fullName) {
        User currentUser = getCurrentUserProfile();
        User updateInfo = new User();
        updateInfo.setFullName(fullName);
        updateInfo.setRole(currentUser.getRole());
        updateInfo.setPassword(null);
        userService.updateUser(currentUser.getId(), updateInfo);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword, String confirmPassword) {
        User currentUser = getCurrentUserProfile();

        if (!userService.checkPassword(currentUser, oldPassword)) {
            throw new RuntimeException("Mật khẩu cũ không đúng.");
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Mật khẩu mới không khớp.");
        }

        User updateInfo = new User();
        updateInfo.setPassword(newPassword);
        updateInfo.setRole(currentUser.getRole());
        updateInfo.setFullName(currentUser.getFullName());
        userService.updateUser(currentUser.getId(), updateInfo);
    }
}