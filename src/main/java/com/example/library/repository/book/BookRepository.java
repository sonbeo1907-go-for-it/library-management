package com.example.library.repository.book;

import com.example.library.model.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findByIsDeletedFalse();

    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

    long countByIsDeletedFalse();

    long countByQuantityGreaterThanAndIsDeletedFalse(int i);
}