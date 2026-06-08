package com.example.library.model.revenue;

import java.math.BigDecimal;

public class RevenueSummaryDto {
    private BigDecimal totalRental;
    private BigDecimal totalFine;
    private BigDecimal total;
    private String formattedTotalRental;
    private String formattedTotalFine;
    private String formattedTotal;

    public RevenueSummaryDto() {}

    // Getters and Setters
    public BigDecimal getTotalRental() { return totalRental; }
    public void setTotalRental(BigDecimal totalRental) { this.totalRental = totalRental; }
    public BigDecimal getTotalFine() { return totalFine; }
    public void setTotalFine(BigDecimal totalFine) { this.totalFine = totalFine; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public String getFormattedTotalRental() { return formattedTotalRental; }
    public void setFormattedTotalRental(String formattedTotalRental) { this.formattedTotalRental = formattedTotalRental; }
    public String getFormattedTotalFine() { return formattedTotalFine; }
    public void setFormattedTotalFine(String formattedTotalFine) { this.formattedTotalFine = formattedTotalFine; }
    public String getFormattedTotal() { return formattedTotal; }
    public void setFormattedTotal(String formattedTotal) { this.formattedTotal = formattedTotal; }
}