package com.example.library.repository.salary;

import com.example.library.model.salary.SalaryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryRecordRepository extends JpaRepository<SalaryRecord, Integer> {
    List<SalaryRecord> findByMonthAndYear(int month, int year);
    Optional<SalaryRecord> findByUserIdAndMonthAndYear(int userId, int month, int year);
}
