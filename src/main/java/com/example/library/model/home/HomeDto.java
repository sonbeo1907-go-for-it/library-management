package com.example.library.model.home;

import com.example.library.model.user.User;

public class HomeDto {
    private User user;
    private boolean librarian;
    // Thống kê cho Librarian
    private long totalBooks;
    private long availableBooks;
    private long borrowedBooks;
    private long overdueBooks;
    private long totalUsers;
    // Thống kê cho Reader
    private long currentlyBorrowed;
    private long overdue;

    public HomeDto() {
    }

    public HomeDto(User user, boolean librarian, long totalBooks, long availableBooks, long borrowedBooks, long overdueBooks, long totalUsers, long currentlyBorrowed, long overdue) {
        this.user = user;
        this.librarian = librarian;
        this.totalBooks = totalBooks;
        this.availableBooks = availableBooks;
        this.borrowedBooks = borrowedBooks;
        this.overdueBooks = overdueBooks;
        this.totalUsers = totalUsers;
        this.currentlyBorrowed = currentlyBorrowed;
        this.overdue = overdue;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLibrarian() {
        return librarian;
    }

    public void setLibrarian(boolean librarian) {
        this.librarian = librarian;
    }

    public long getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(long totalBooks) {
        this.totalBooks = totalBooks;
    }

    public long getAvailableBooks() {
        return availableBooks;
    }

    public void setAvailableBooks(long availableBooks) {
        this.availableBooks = availableBooks;
    }

    public long getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(long borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public long getOverdueBooks() {
        return overdueBooks;
    }

    public void setOverdueBooks(long overdueBooks) {
        this.overdueBooks = overdueBooks;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getCurrentlyBorrowed() {
        return currentlyBorrowed;
    }

    public void setCurrentlyBorrowed(long currentlyBorrowed) {
        this.currentlyBorrowed = currentlyBorrowed;
    }

    public long getOverdue() {
        return overdue;
    }

    public void setOverdue(long overdue) {
        this.overdue = overdue;
    }
}