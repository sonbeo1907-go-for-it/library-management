package com.example.library.repository.salary;

import com.example.library.model.salary.SalaryConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalaryConfigRepository extends JpaRepository<SalaryConfig, Integer> {
    Optional<SalaryConfig> findByUserId(int userId);
}

