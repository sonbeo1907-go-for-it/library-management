package com.example.library.repository.book;

import com.example.library.model.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findByIsDeletedFalse();

    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

    long countByIsDeletedFalse();

    long countByQuantityGreaterThanAndIsDeletedFalse(int i);

    // Thêm method trả về Page
    Page<Book> findByIsDeletedFalse(Pageable pageable);

    Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author, Pageable pageable);

    @Query(value = "SELECT * FROM book WHERE is_deleted = false ORDER BY RAND() LIMIT ?1", nativeQuery = true)
    List<Book> findRandomBooks(int count);
}