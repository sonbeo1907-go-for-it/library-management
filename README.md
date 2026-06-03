# 📚 Library Management System

Hệ thống quản lý thư viện nội bộ, xây dựng bằng Spring Boot + Thymeleaf + MySQL. Dự án hỗ trợ hai vai trò: **Thủ thư (Librarian)** và **Độc giả (Reader)**, với các chức năng mượn/trả sách, quản lý sách, đánh giá, thống kê và quản lý người dùng.

---

## 🚀 Tính năng chính
### 👥 Phân quyền
- **Thủ thư (Librarian)**: quản lý sách, quản lý người dùng, xem thống kê, xác nhận trả sách, xem sách quá hạn.
- **Độc giả (Reader)**: xem danh sách sách, mượn sách cho bản thân, xem lịch sử mượn, đánh giá sách.
### 📖 Quản lý sách
- Thêm, sửa, xóa sách (soft delete).
- Upload ảnh bìa.
- Tìm kiếm theo tên hoặc tác giả.
### 🔄 Mượn / Trả sách
- Mượn sách (kiểm tra số lượng tồn).
- Tự động tính phí quá hạn (2000 VNĐ/ngày).
- Xác nhận trả sách (dành cho thủ thư).
- Lịch sử mượn/trả cho cả hai vai trò.
### ⭐ Đánh giá
- Chỉ được đánh giá sau khi đã trả sách.
- Mỗi độc giả chỉ được đánh giá 1 lần/sách.
- Hiển thị điểm trung bình và danh sách đánh giá.
### 📊 Thống kê (Thủ thư)
- Tổng số sách, sách có sẵn, sách đang mượn, quá hạn, số độc giả.
- Trang dashboard trực quan.
### 👤 Quản lý tài khoản
- Đăng ký (mặc định role READER).
- Xem/sửa hồ sơ cá nhân.
- Đổi mật khẩu.

---

## 🛠️ Công nghệ sử dụng
| Lớp          | Công nghệ                                |
|--------------|------------------------------------------|
| Backend      | Java 17, Spring Boot 3.x, Spring Security, Spring Data JPA |
| Frontend     | Thymeleaf, Bootstrap 5, HTML/CSS/JS      |
| Database     | MySQL                                    |
| Build Tool   | Maven                                    |
| Bảo mật      | BCrypt, Spring Security form login       |
| Khác         | Lombok (tuỳ chọn), WebJars (Bootstrap)   |

---

## ⚙️ Cài đặt & Chạy ứng dụng
### Yêu cầu
- JDK 17 trở lên
- MySQL 8.0+
- Maven 3.6+

### Bước 1: Tạo database
```
sql
CREATE DATABASE library_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
### Bước 2: Cấu hình môi trường
```
DB_URL=jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
APP_SECRET=mySecretKey123!
```

### Bước 3: Build và chạy
```
mvn clean spring-boot:run
```

### Tài khoản mặc định (tự động tạo nếu chưa tồn tại)
```
Thủ thư: admin/ adminpassword
```

### Cấu trúc dự án
```
src/main/java/com/example/library
├── config/          # Cấu hình Spring Security, Web, Dotenv
├── constant/        # Hằng số URL, Role, Screen name
├── controller/      # Các controller
├── dto/             # Data Transfer Objects
├── model/           # Entity và Enum
├── repository/      # Spring Data JPA repositories
├── service/         # Interface và Implementation của service
src/main/resources
├── static/          # CSS, JS, hình ảnh tĩnh
├── templates/       # Thymeleaf views (layouts, fragments, pages)
└── application.properties
```

### Sơ đồ luông màn hình
```
[Login] <--> [Register]
   |
   v
[Home] --(menu)--> [Book List] --(click)--> [Book Detail]
   |                    |                      |
   |                    |                      +-->(Mượn)--> [Borrow Form] --> [History]
   |                    |                      +-->(Đánh giá)--> [Review Form]
   |                    +-->(Thêm/Sửa)--> [Book Form]
   |                    +-->(Xóa) --> redirect Book List
   |
   +-->(Lịch sử)--> [History] --(Trả)--> [Return Confirm] --> [History]
   |
   +-->(Dashboard)--> [Dashboard] (LIBRARIAN)
   |
   +-->(Quản lý User)--> [User List] --(Thêm/Sửa)--> [User Form]
   |
   +-->(Profile)--> [Profile] --(Sửa)--> [Profile Edit]
                  |            +-->(Đổi mật khẩu)--> [Change Password]
                  +-->(Logout)--> [Login]
```

### Ghi chú
- Mật khẩu được mã hoá bằng BCrypt.
- Ảnh sách được lưu trong thư mục uploads/ (tự động tạo).
- Hệ thống sử dụng session-based authentication, không dùng JWT.
- Có thể tìm thấy các hằng số quan trọng trong package constant.

### Đóng góp
Bài tập cá nhân trong môn học. Không nhận pull request bên ngoài.

### License
Dự án chỉ dành cho mục đích học tập.
