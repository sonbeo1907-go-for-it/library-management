package com.example.library.model.borrow;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReturnConfirmDto {
    private int recordId;
    private String borrowCode;           // thêm sau
    private String bookTitle;
    private String bookAuthor;
    private String imageFilename;
    private String borrowerName;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate today;
    private long daysLate;
    private BigDecimal fine;
    private String formattedFine;

    public ReturnConfirmDto() {}

    // Getters và Setters đầy đủ
    public int getRecordId() { return recordId; }
    public void setRecordId(int recordId) { this.recordId = recordId; }

    public String getBorrowCode() { return borrowCode; }
    public void setBorrowCode(String borrowCode) { this.borrowCode = borrowCode; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getBookAuthor() { return bookAuthor; }
    public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }

    public String getImageFilename() { return imageFilename; }
    public void setImageFilename(String imageFilename) { this.imageFilename = imageFilename; }

    public String getBorrowerName() { return borrowerName; }
    public void setBorrowerName(String borrowerName) { this.borrowerName = borrowerName; }

    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getToday() { return today; }
    public void setToday(LocalDate today) { this.today = today; }

    public long getDaysLate() { return daysLate; }
    public void setDaysLate(long daysLate) { this.daysLate = daysLate; }

    public BigDecimal getFine() { return fine; }
    public void setFine(BigDecimal fine) { this.fine = fine; }

    public String getFormattedFine() { return formattedFine; }
    public void setFormattedFine(String formattedFine) { this.formattedFine = formattedFine; }
}