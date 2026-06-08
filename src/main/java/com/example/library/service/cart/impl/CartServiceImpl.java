package com.example.library.service.cart.impl;

import com.example.library.constant.ApplicationConstants;
import com.example.library.model.book.Book;
import com.example.library.model.borrow.BorrowRecord;
import com.example.library.model.cart.*;
import com.example.library.model.user.User;
import com.example.library.repository.book.BookRepository;
import com.example.library.repository.cart.CartRepository;
import com.example.library.repository.user.UserRepository;
import com.example.library.service.borrow.BorrowService;
import com.example.library.service.cart.CartService;
import com.example.library.service.validation.ValidationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowService borrowService;
    private final ValidationService validationService;

    public CartServiceImpl(CartRepository cartRepository, BookRepository bookRepository,
                           UserRepository userRepository, BorrowService borrowService,
                           ValidationService validationService) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.borrowService = borrowService;
        this.validationService = validationService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Cart getOrCreateActiveCart(User user) {
        return cartRepository.findByUserIdAndStatus(user.getId(), CartStatus.ACTIVE)
                .orElseGet(() -> {
                    Cart newCart = new Cart(user);
                    return cartRepository.save(newCart);
                });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CartItem addItem(int cartId, int bookId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));
        if (cart.getStatus() != CartStatus.ACTIVE) {
            throw new RuntimeException("Giỏ hàng đã bị khóa.");
        }
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Sách không tồn tại"));
        if (book.getQuantity() <= 0) {
            throw new RuntimeException("Sách đã hết.");
        }
        boolean alreadyInCart = cart.getItems().stream()
                .anyMatch(item -> item.getBook().getId() == bookId);
        if (alreadyInCart) {
            throw new RuntimeException("Sách này đã có trong giỏ.");
        }

        User user = cart.getUser();
        validationService.validateNotAlreadyBorrowed(bookId, user.getId());
        validationService.validateUserNotOverdue(user.getId());

        CartItem item = new CartItem(cart, book, 1);
        cart.addItem(item);
        cartRepository.save(cart);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removeItem(int cartItemId, User user) {
        Cart cart = cartRepository.findByUserIdAndStatus(user.getId(), CartStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng"));
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId() == cartItemId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sản phẩm không có trong giỏ"));
        cart.removeItem(item);
        cartRepository.save(cart);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CartDto submitCart(int userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng trống hoặc đã nộp"));
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống.");
        }
        String code = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        cart.setCartCode(code);
        cart.setStatus(CartStatus.SUBMITTED);
        cart.setSubmittedAt(LocalDateTime.now());
        cartRepository.save(cart);
        return convertToDto(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public CartDto getCartDtoByCode(String cartCode) {
        Cart cart = cartRepository.findByCartCode(cartCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng với mã: " + cartCode));
        return convertToDto(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public CartDto getCartDto(int userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE).orElse(null);
        if (cart == null) return new CartDto();
        return convertToDto(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartDto> getSubmittedCarts() {
        List<Cart> carts = cartRepository.findByStatusOrderBySubmittedAtAsc(CartStatus.SUBMITTED);
        return carts.stream().map(this::convertToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Cart findById(int cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng"));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void approveCart(int cartId, int approvedById) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));
        if (cart.getStatus() != CartStatus.SUBMITTED) {
            throw new RuntimeException("Giỏ hàng không ở trạng thái chờ duyệt");
        }
        User approver = userRepository.findById(approvedById)
                .orElseThrow(() -> new RuntimeException("Người duyệt không tồn tại"));
        User borrower = cart.getUser();

        for (CartItem item : cart.getItems()) {
            Book book = item.getBook();
            validationService.validateBookExists(book.getId());
            validationService.validateBookAvailable(book.getId());
            validationService.validateNotAlreadyBorrowed(book.getId(), borrower.getId());
            validationService.validateUserNotOverdue(borrower.getId());
        }

        BigDecimal feePerBook = BigDecimal.valueOf(ApplicationConstants.BORROW_FEE_PER_BOOK);
        BigDecimal totalFee = BigDecimal.ZERO;
        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plusDays(ApplicationConstants.BORROW_DURATION_DAYS);

        for (CartItem item : cart.getItems()) {
            Book book = item.getBook();
            book.setQuantity(book.getQuantity() - 1);
            bookRepository.save(book);
            borrowService.createBorrowRecord(book, borrower, today, dueDate, feePerBook);
            totalFee = totalFee.add(feePerBook);
        }

        cart.setStatus(CartStatus.APPROVED);
        cart.setApprovedBy(approver);
        cart.setApprovedAt(LocalDateTime.now());
        cart.setTotalFee(totalFee);
        cartRepository.save(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartDto> getCartsByUser(int userId) {
        List<Cart> carts = cartRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return carts.stream().map(this::convertToDto).toList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void rejectCart(int cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));
        if (cart.getStatus() != CartStatus.SUBMITTED) {
            throw new RuntimeException("Giỏ hàng không ở trạng thái chờ duyệt");
        }
        cart.setStatus(CartStatus.REJECTED);
        cartRepository.save(cart);
    }

    private CartDto convertToDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setCartId(cart.getId());
        dto.setCartCode(cart.getCartCode());
        dto.setStatus(cart.getStatus());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setSubmittedAt(cart.getSubmittedAt());
        dto.setUsername(cart.getUser().getUsername());
        dto.setFullName(cart.getUser().getFullName());

        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        BigDecimal feePerBook = BigDecimal.valueOf(ApplicationConstants.BORROW_FEE_PER_BOOK);

        List<CartItemDto> itemDtos = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            CartItemDto itemDto = new CartItemDto();
            itemDto.setItemId(item.getId());
            itemDto.setBookId(item.getBook().getId());
            itemDto.setBookTitle(item.getBook().getTitle());
            itemDto.setBookAuthor(item.getBook().getAuthor());
            itemDto.setImageFilename(item.getBook().getImageFilename());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setFeePerItem(feePerBook);
            itemDto.setFormattedFeePerItem(numberFormat.format(feePerBook) + " VNĐ");  // format
            itemDtos.add(itemDto);
        }
        dto.setItems(itemDtos);

        // Tính tổng phí hiển thị
        BigDecimal totalFee;
        if (cart.getStatus() == CartStatus.APPROVED && cart.getTotalFee() != null) {
            totalFee = cart.getTotalFee();
        } else {
            totalFee = feePerBook.multiply(BigDecimal.valueOf(cart.getItems().size()));
        }
        dto.setTotalFee(totalFee);
        dto.setFormattedTotalFee(numberFormat.format(totalFee) + " VNĐ");

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CartDto> getSubmittedCarts(Pageable pageable) {
        Page<Cart> carts = cartRepository.findByStatusOrderBySubmittedAtAsc(CartStatus.SUBMITTED, pageable);
        return carts.map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CartDto> getCartsByUser(int userId, Pageable pageable) {
        Page<Cart> carts = cartRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return carts.map(this::convertToDto);
    }
}