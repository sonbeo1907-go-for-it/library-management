package com.example.library.service;

import com.example.library.model.Book;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService {

    List<Book> findAll();

    List<Book> search(String keyword);

    Book findById(int id);

    Book save(Book book, MultipartFile imageFile) throws IOException;

    Book update(int id, Book bookDetails, MultipartFile imageFile) throws IOException;

    void deleteById(int id);
}