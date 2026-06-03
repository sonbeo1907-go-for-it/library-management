package com.example.library.repository;

import com.example.library.model.Role;
import com.example.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    long countByRoleAndIsDeletedFalse(Role role);

    long countByIsDeletedFalse();
}