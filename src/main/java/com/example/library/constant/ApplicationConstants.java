package com.example.library.constant;

public final class ApplicationConstants {

    // ===== Đường dẫn URL =====
    public static final String HOME_URL = "/home";
    public static final String REGISTER_URL = "/register";
    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";

    // Book
    public static final String BOOK_LIST_URL = "/books";
    public static final String BOOK_NEW_URL = "/books/new";
    public static final String BOOK_EDIT_URL = "/books/{id}/edit";
    public static final String BOOK_SAVE_URL = "/books/save";
    public static final String BOOK_DELETE_URL = "/books/{id}/delete";
    public static final String BOOK_DETAIL_URL = "/books/{id}";

    // Borrow
    public static final String BORROW_URL = "/borrow";               // GET/POST
    public static final String RETURN_URL = "/return/{id}";          // GET confirm, POST
    public static final String HISTORY_URL = "/history";
    public static final String OVERDUE_URL = "/overdue";

    // Review
    public static final String REVIEW_URL = "/review";               // GET form?bookId=...
    public static final String REVIEW_SAVE_URL = "/review/save";     // POST

    // User management (LIBRARIAN)
    public static final String USER_LIST_URL = "/users";
    public static final String USER_NEW_URL = "/users/new";
    public static final String USER_EDIT_URL = "/users/{id}/edit";
    public static final String USER_SAVE_URL = "/users/save";        // POST
    public static final String USER_DELETE_URL = "/users/{id}/delete";

    // ===== Upload =====
    public static final String UPLOAD_DIR = "uploads";
    public static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB

    // ===== Phân trang =====
    public static final int DEFAULT_PAGE_SIZE = 10;

    // ===== Thời gian mượn mặc định =====
    public static final int BORROW_DURATION_DAYS = 14;
    public static final long FINE_PER_DAY = 2000; // VND
    public static final String DASHBOARD_URL = "/dashboard";

    public static final String PROFILE_URL = "/profile";
    public static final String PROFILE_EDIT_URL = "/profile/edit";
    public static final String CHANGE_PASSWORD_URL = "/profile/change-password";
    // Không cho khởi tạo
    private ApplicationConstants() {}
}