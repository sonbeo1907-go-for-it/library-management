package com.example.library.service.user;

import com.example.library.model.user.User;

public interface CurrentUserService {
    String getCurrentUsername();
    User getCurrentUser();
    boolean hasRole(String role);
    boolean isAuthenticated();
}