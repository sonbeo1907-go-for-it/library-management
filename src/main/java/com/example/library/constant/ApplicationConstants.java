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

    // Cart
    public static final String CART_URL = "/cart";
    public static final String CART_ADD_URL = "/cart/add/{bookId}";
    public static final String CART_REMOVE_URL = "/cart/remove/{cartItemId}";
    public static final String CART_SUBMIT_URL = "/cart/submit";

    // Approval
    public static final String APPROVAL_LIST_URL = "/approvals";
    public static final String APPROVAL_APPROVE_URL = "/approvals/approve/{cartId}";
    public static final String APPROVAL_REJECT_URL = "/approvals/reject/{cartId}";

    // ===== Upload =====
    public static final String UPLOAD_DIR = "uploads";
    public static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB

    // ===== Phân trang =====
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final long BORROW_FEE_PER_BOOK = 5000; // VND

    // ===== Thời gian mượn mặc định =====
    public static final int BORROW_DURATION_DAYS = 14;
    public static final long FINE_PER_DAY = 2000; // VND

    public static final String PROFILE_URL = "/profile";
    public static final String PROFILE_EDIT_URL = "/profile/edit";
    public static final String CHANGE_PASSWORD_URL = "/profile/change-password";
    // Không cho khởi tạo

    // ===== Lương (Admin) =====
    public static final String ADMIN_SALARIES_URL = "/admin/salaries";
    public static final String ADMIN_SALARIES_CALCULATE_URL = "/admin/salaries/calculate";
    public static final String ADMIN_SALARIES_PAY_URL = "/admin/salaries/{id}/pay";
    public static final String ADMIN_SALARIES_CANCEL_URL = "/admin/salaries/{id}/cancel";
    public static final String ADMIN_SALARIES_CONFIG_URL = "/admin/salaries/config/{userId}";

    // Admin
    public static final String ADMIN_REVENUE_URL = "/admin/revenue";

    public static final String MY_CARTS_URL = "/my-carts";
    private ApplicationConstants() {}
}