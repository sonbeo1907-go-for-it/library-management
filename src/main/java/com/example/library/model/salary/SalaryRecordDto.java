package com.example.library.model.salary;

import java.math.BigDecimal;

public class SalaryRecordDto {
    private int id;
    private int userId;
    private String fullName;
    private BigDecimal baseSalary;
    private BigDecimal commissionAmount;
    private BigDecimal bonusAmount;
    private BigDecimal totalSalary;
    private String status;

    // Các trường đã format
    private String formattedBaseSalary;
    private String formattedCommissionAmount;
    private String formattedBonusAmount;
    private String formattedTotalSalary;

    public SalaryRecordDto() {}

    // Getters & Setters đầy đủ
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public BigDecimal getBaseSalary() { return baseSalary; }
    public void setBaseSalary(BigDecimal baseSalary) { this.baseSalary = baseSalary; }
    public BigDecimal getCommissionAmount() { return commissionAmount; }
    public void setCommissionAmount(BigDecimal commissionAmount) { this.commissionAmount = commissionAmount; }
    public BigDecimal getBonusAmount() { return bonusAmount; }
    public void setBonusAmount(BigDecimal bonusAmount) { this.bonusAmount = bonusAmount; }
    public BigDecimal getTotalSalary() { return totalSalary; }
    public void setTotalSalary(BigDecimal totalSalary) { this.totalSalary = totalSalary; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFormattedBaseSalary() { return formattedBaseSalary; }
    public void setFormattedBaseSalary(String formattedBaseSalary) { this.formattedBaseSalary = formattedBaseSalary; }
    public String getFormattedCommissionAmount() { return formattedCommissionAmount; }
    public void setFormattedCommissionAmount(String formattedCommissionAmount) { this.formattedCommissionAmount = formattedCommissionAmount; }
    public String getFormattedBonusAmount() { return formattedBonusAmount; }
    public void setFormattedBonusAmount(String formattedBonusAmount) { this.formattedBonusAmount = formattedBonusAmount; }
    public String getFormattedTotalSalary() { return formattedTotalSalary; }
    public void setFormattedTotalSalary(String formattedTotalSalary) { this.formattedTotalSalary = formattedTotalSalary; }
}