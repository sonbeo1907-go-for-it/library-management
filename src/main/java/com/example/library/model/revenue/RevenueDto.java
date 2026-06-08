package com.example.library.model.revenue;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RevenueDto {
    private LocalDate date;
    private BigDecimal rentalRevenue;   // tổng phí mượn trong ngày
    private BigDecimal fineRevenue;     // tổng phí phạt trong ngày
    private BigDecimal totalRevenue;    // tổng cộng

    private String formattedRentalRevenue;
    private String formattedFineRevenue;
    private String formattedTotalRevenue;

    public RevenueDto() {}

    public RevenueDto(LocalDate date, BigDecimal rentalRevenue, BigDecimal fineRevenue) {
        this.date = date;
        this.rentalRevenue = rentalRevenue;
        this.fineRevenue = fineRevenue;
        this.totalRevenue = rentalRevenue.add(fineRevenue);
    }

    // Getters, setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public BigDecimal getRentalRevenue() { return rentalRevenue; }
    public void setRentalRevenue(BigDecimal rentalRevenue) { this.rentalRevenue = rentalRevenue; }
    public BigDecimal getFineRevenue() { return fineRevenue; }
    public void setFineRevenue(BigDecimal fineRevenue) { this.fineRevenue = fineRevenue; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public String getFormattedRentalRevenue() { return formattedRentalRevenue; }
    public void setFormattedRentalRevenue(String formattedRentalRevenue) { this.formattedRentalRevenue = formattedRentalRevenue; }
    public String getFormattedFineRevenue() { return formattedFineRevenue; }
    public void setFormattedFineRevenue(String formattedFineRevenue) { this.formattedFineRevenue = formattedFineRevenue; }
    public String getFormattedTotalRevenue() { return formattedTotalRevenue; }
    public void setFormattedTotalRevenue(String formattedTotalRevenue) { this.formattedTotalRevenue = formattedTotalRevenue; }
}