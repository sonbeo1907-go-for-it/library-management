package com.example.library.service;

import com.example.library.model.User;

public interface CurrentUserService {
    String getCurrentUsername();
    User getCurrentUser();
    boolean hasRole(String role);
    boolean isAuthenticated();
}