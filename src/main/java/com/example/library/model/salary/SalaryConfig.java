package com.example.library.model.salary;

import com.example.library.model.user.User;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "salary_config")
public class SalaryConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)  // Mỗi thủ thư có một cấu hình riêng
    private User user;

    @Column(name = "base_salary", precision = 12, scale = 2)
    private BigDecimal baseSalary = BigDecimal.ZERO; // Lương cứng theo tháng

    @Column(name = "commission_rate", precision = 5, scale = 3)
    private BigDecimal commissionRate = BigDecimal.ZERO; // Tỷ lệ hoa hồng (ví dụ 0.05 = 5%)

    @Column(name = "bonus_per_approval", precision = 10, scale = 2)
    private BigDecimal bonusPerApproval = BigDecimal.ZERO; // Thưởng mỗi lượt duyệt mượn

    @Column(name = "bonus_per_return", precision = 10, scale = 2)
    private BigDecimal bonusPerReturn = BigDecimal.ZERO; // Thưởng mỗi lượt xác nhận trả

    // Constructors
    public SalaryConfig() {}

    public SalaryConfig(User user) {
        this.user = user;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public BigDecimal getBaseSalary() { return baseSalary; }
    public void setBaseSalary(BigDecimal baseSalary) { this.baseSalary = baseSalary; }
    public BigDecimal getCommissionRate() { return commissionRate; }
    public void setCommissionRate(BigDecimal commissionRate) { this.commissionRate = commissionRate; }
    public BigDecimal getBonusPerApproval() { return bonusPerApproval; }
    public void setBonusPerApproval(BigDecimal bonusPerApproval) { this.bonusPerApproval = bonusPerApproval; }
    public BigDecimal getBonusPerReturn() { return bonusPerReturn; }
    public void setBonusPerReturn(BigDecimal bonusPerReturn) { this.bonusPerReturn = bonusPerReturn; }
}