// ReturnConfirmDto.java
package com.example.library.model.borrow;

import com.example.library.model.borrow.BorrowRecord;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReturnConfirmDto {
    private BorrowRecord record;
    private LocalDate today;
    private long daysLate;
    private BigDecimal fine;
    private String formattedFine;

    public ReturnConfirmDto(BorrowRecord record, LocalDate today, long daysLate, BigDecimal fine, String formattedFine) {
        this.record = record;
        this.today = today;
        this.daysLate = daysLate;
        this.fine = fine;
        this.formattedFine = formattedFine;
    }


    public ReturnConfirmDto() {
    }

    public BorrowRecord getRecord() {
        return record;
    }

    public void setRecord(BorrowRecord record) {
        this.record = record;
    }

    public LocalDate getToday() {
        return today;
    }

    public void setToday(LocalDate today) {
        this.today = today;
    }

    public long getDaysLate() {
        return daysLate;
    }

    public void setDaysLate(long daysLate) {
        this.daysLate = daysLate;
    }

    public BigDecimal getFine() {
        return fine;
    }

    public void setFine(BigDecimal fine) {
        this.fine = fine;
    }

    public String getFormattedFine() {
        return formattedFine;
    }

    public void setFormattedFine(String formattedFine) {
        this.formattedFine = formattedFine;
    }
}