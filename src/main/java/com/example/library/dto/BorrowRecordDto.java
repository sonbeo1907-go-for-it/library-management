// File: src/main/java/com/example/library/dto/BorrowRecordDto.java
package com.example.library.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BorrowRecordDto {
    private int id;
    private String bookTitle;
    private int bookId;
    private String borrowerName;
    private int borrowerId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;       // "BORROWED" hoặc "RETURNED"
    private BigDecimal fineAmount;

    public BorrowRecordDto() {}

    public BorrowRecordDto(int id, String bookTitle, int bookId, String borrowerName, int borrowerId,
                           LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate, String status, BigDecimal fineAmount) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.bookId = bookId;
        this.borrowerName = borrowerName;
        this.borrowerId = borrowerId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.fineAmount = fineAmount;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public String getBorrowerName() { return borrowerName; }
    public void setBorrowerName(String borrowerName) { this.borrowerName = borrowerName; }
    public int getBorrowerId() { return borrowerId; }
    public void setBorrowerId(int borrowerId) { this.borrowerId = borrowerId; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getFineAmount() { return fineAmount; }
    public void setFineAmount(BigDecimal fineAmount) { this.fineAmount = fineAmount; }
}