package com.example.library.model.borrow;

import com.example.library.model.book.Book;
import com.example.library.model.user.User;

import java.time.LocalDate;
import java.util.List;

public class BorrowFormDto {
    private Book book;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private List<User> users; // chỉ có khi là librarian

    // constructor, getters, setters
    public BorrowFormDto(Book book, LocalDate borrowDate, LocalDate dueDate, List<User> users) {
        this.book = book;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.users = users;
    }


    public BorrowFormDto() {
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}