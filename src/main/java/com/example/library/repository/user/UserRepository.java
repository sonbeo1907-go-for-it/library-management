package com.example.library.repository.user;

import com.example.library.model.user.Role;
import com.example.library.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    long countByRoleAndIsDeletedFalse(Role role);

    long countByIsDeletedFalse();

    List<User> findByIsDeletedFalse();
}