package com.example.library.service.book.impl;

import com.example.library.model.book.Book;
import com.example.library.model.book.BookWithRatingDto;
import com.example.library.repository.book.BookRepository;
import com.example.library.repository.review.ReviewRepository;
import com.example.library.service.book.BookService;
import com.example.library.service.validation.ValidationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ValidationService validationService;
    private final Path uploadPath;
    private final ReviewRepository reviewRepository;

    public BookServiceImpl(BookRepository bookRepository,ValidationService validationService ,
                           @Value("${app.upload.dir}") String uploadDir, ReviewRepository reviewRepository) {
        this.bookRepository = bookRepository;
        this.validationService = validationService;
        this.uploadPath = Paths.get(uploadDir);
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Không thể tạo thư mục uploads: " + uploadPath, e);
        }
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findByIsDeletedFalse();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> search(String keyword) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public Book findById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public Book save(Book book, MultipartFile imageFile) throws IOException {
        handleImageUpload(book, imageFile);
        return bookRepository.save(book);
    }

    @Override
    public Book update(int id, Book bookDetails, MultipartFile imageFile) throws IOException {
        // Sử dụng ValidationService để kiểm tra tồn tại
        validationService.validateBookExists(id);

        Book existingBook = bookRepository.findById(id).orElseThrow(); // chắc chắn có
        existingBook.setTitle(bookDetails.getTitle());
        existingBook.setAuthor(bookDetails.getAuthor());
        existingBook.setPublisher(bookDetails.getPublisher());
        existingBook.setYear(bookDetails.getYear());
        existingBook.setQuantity(bookDetails.getQuantity());

        if (imageFile != null && !imageFile.isEmpty()) {
            deleteOldImage(existingBook.getImageFilename());
            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            existingBook.setImageFilename(filename);
        }

        return bookRepository.save(existingBook);
    }

    @Override
    public void deleteById(int id) {
        validationService.validateBookExists(id);
        Book book = bookRepository.findById(id).orElseThrow();
        book.setDeleted(true);
        bookRepository.save(book);
    }

    // ================== HELPER METHODS ==================

    private void handleImageUpload(Book book, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            book.setImageFilename(filename);
        }
    }

    private void deleteOldImage(String imageFilename) {
        if (imageFilename != null && !imageFilename.isEmpty()) {
            try {
                Path oldFilePath = uploadPath.resolve(imageFilename);
                Files.deleteIfExists(oldFilePath);
            } catch (IOException e) {
                System.err.println("Không thể xóa ảnh cũ: " + imageFilename + " - " + e.getMessage());
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Book> searchBooks(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.isBlank()) {
            return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword, pageable);
        }
        return bookRepository.findByIsDeletedFalse(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getRandomBooks(int count) {
        return bookRepository.findRandomBooks(count);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookWithRatingDto> getTopRatedBooks(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Object[]> rows = reviewRepository.findTopBooksByRating(pageable);
        return rows.stream().map(row -> {
            BookWithRatingDto dto = new BookWithRatingDto();
            dto.setId((int) row[0]);
            dto.setTitle((String) row[1]);
            dto.setAuthor((String) row[2]);
            dto.setImageFilename((String) row[3]);
            dto.setAverageRating(((Number) row[4]).doubleValue());
            dto.setReviewCount(((Number) row[5]).intValue());
            return dto;
        }).collect(Collectors.toList());
    }
}