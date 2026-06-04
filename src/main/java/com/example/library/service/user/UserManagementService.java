package com.example.library.service.user;

import com.example.library.model.user.User;
import java.util.List;

public interface UserManagementService {
    List<User> getAllUsers();
    User getUserById(int id);
    void createUser(User user);
    void updateUser(int id, User user);
    void softDeleteUser(int id);
}