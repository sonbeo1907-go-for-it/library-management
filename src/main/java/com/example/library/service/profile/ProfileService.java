package com.example.library.service.profile;

import com.example.library.model.user.User;

public interface ProfileService {
    User getCurrentUserProfile();
    void updateFullName(String fullName);
    void changePassword(String oldPassword, String newPassword, String confirmPassword);
}