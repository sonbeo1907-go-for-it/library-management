package com.example.library.service.user.impl;

import com.example.library.model.user.User;
import com.example.library.service.user.UserManagementService;
import com.example.library.service.user.UserService;
import com.example.library.service.validation.ValidationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    private final UserService userService;
    private final ValidationService validationService;

    public UserManagementServiceImpl(UserService userService, ValidationService validationService) {
        this.userService = userService;
        this.validationService = validationService;
    }

    @Override
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @Override
    public User getUserById(int id) {
        validationService.validateUserExists(id);
        return userService.findById(id).orElseThrow();
    }

    @Override
    public void createUser(User user) {
        userService.createUser(user);
    }

    @Override
    public void updateUser(int id, User user) {
        validationService.validateUserExists(id);
        userService.updateUser(id, user);
    }

    @Override
    public void softDeleteUser(int id) {
        validationService.validateUserExists(id);
        userService.softDeleteById(id);
    }
}