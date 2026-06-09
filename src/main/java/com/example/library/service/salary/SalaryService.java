package com.example.library.service.salary;

import com.example.library.model.salary.*;

import java.util.List;

public interface SalaryService {
    void calculateMonthlySalary(int year, int month);
    void paySalary(int recordId);
    void cancelSalary(int recordId);
    SalaryConfig getSalaryConfig(int userId);
    void saveSalaryConfig(SalaryConfig config);
    List<LibrarianSalaryDto> getLibrarianSalaries(int year, int month, String search);
    SalaryConfigDto getSalaryConfigDto(int userId);
    void saveSalaryConfigDto(SalaryConfigDto dto);
}