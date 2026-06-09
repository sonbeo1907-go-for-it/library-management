package com.example.library.model.salary;

public class LibrarianSalaryDto {
    private int userId;
    private String fullName;
    private boolean hasConfig;
    private boolean hasSalaryRecord;
    private SalaryRecordDto salaryRecord; // null nếu chưa có lương

    public LibrarianSalaryDto() {}

    // Getters & Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public boolean isHasConfig() { return hasConfig; }
    public void setHasConfig(boolean hasConfig) { this.hasConfig = hasConfig; }
    public boolean isHasSalaryRecord() { return hasSalaryRecord; }
    public void setHasSalaryRecord(boolean hasSalaryRecord) { this.hasSalaryRecord = hasSalaryRecord; }
    public SalaryRecordDto getSalaryRecord() { return salaryRecord; }
    public void setSalaryRecord(SalaryRecordDto salaryRecord) { this.salaryRecord = salaryRecord; }
}