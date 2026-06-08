package com.example.library.service.book;

import com.example.library.model.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<Book> searchBooks(String keyword, Pageable pageable);

    List<Book> getRandomBooks(int count);
}