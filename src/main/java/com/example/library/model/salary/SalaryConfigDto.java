package com.example.library.model.salary;

import java.math.BigDecimal;

public class SalaryConfigDto {
    private int userId;
    private String fullName;
    private BigDecimal baseSalary;
    private BigDecimal commissionRate;
    private BigDecimal bonusPerApproval;
    private BigDecimal bonusPerReturn;

    // Getters & Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public BigDecimal getBaseSalary() { return baseSalary; }
    public void setBaseSalary(BigDecimal baseSalary) { this.baseSalary = baseSalary; }
    public BigDecimal getCommissionRate() { return commissionRate; }
    public void setCommissionRate(BigDecimal commissionRate) { this.commissionRate = commissionRate; }
    public BigDecimal getBonusPerApproval() { return bonusPerApproval; }
    public void setBonusPerApproval(BigDecimal bonusPerApproval) { this.bonusPerApproval = bonusPerApproval; }
    public BigDecimal getBonusPerReturn() { return bonusPerReturn; }
    public void setBonusPerReturn(BigDecimal bonusPerReturn) { this.bonusPerReturn = bonusPerReturn; }
}