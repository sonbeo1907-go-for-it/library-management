package com.example.library.model.salary;

import com.example.library.model.user.User;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "salary_record")
public class SalaryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "month", nullable = false)
    private int month; // 1-12

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "base_salary", precision = 12, scale = 2)
    private BigDecimal baseSalary = BigDecimal.ZERO;

    @Column(name = "commission_amount", precision = 12, scale = 2)
    private BigDecimal commissionAmount = BigDecimal.ZERO; // Hoa hồng từ phí mượn

    @Column(name = "bonus_amount", precision = 12, scale = 2)
    private BigDecimal bonusAmount = BigDecimal.ZERO; // Tổng thưởng (duyệt + trả)

    @Column(name = "total_salary", precision = 12, scale = 2)
    private BigDecimal totalSalary = BigDecimal.ZERO; // Tổng lương = base + commission + bonus

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SalaryStatus status = SalaryStatus.PENDING;

    @Column(name = "approved_by")
    private String approvedBy; // Admin duyệt lương

    @Column(name = "notes", length = 500)
    private String notes;

    // Constructors
    public SalaryRecord() {}

    public SalaryRecord(User user, int month, int year) {
        this.user = user;
        this.month = month;
        this.year = year;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public BigDecimal getBaseSalary() { return baseSalary; }
    public void setBaseSalary(BigDecimal baseSalary) { this.baseSalary = baseSalary; }
    public BigDecimal getCommissionAmount() { return commissionAmount; }
    public void setCommissionAmount(BigDecimal commissionAmount) { this.commissionAmount = commissionAmount; }
    public BigDecimal getBonusAmount() { return bonusAmount; }
    public void setBonusAmount(BigDecimal bonusAmount) { this.bonusAmount = bonusAmount; }
    public BigDecimal getTotalSalary() { return totalSalary; }
    public void setTotalSalary(BigDecimal totalSalary) { this.totalSalary = totalSalary; }
    public SalaryStatus getStatus() { return status; }
    public void setStatus(SalaryStatus status) { this.status = status; }
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}