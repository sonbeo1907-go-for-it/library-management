package com.example.library.service.book;

import com.example.library.model.book.Book;
import com.example.library.model.book.BookDetailDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookPageService {
    List<Book> getBooksForList(String keyword);
    BookDetailDto getBookDetail(int bookId);
    Book saveBook(Book book, MultipartFile imageFile) throws IOException;
    Book updateBook(int id, Book bookDetails, MultipartFile imageFile) throws IOException;
    void deleteBook(int id);
    Book getBookById(int id);
}