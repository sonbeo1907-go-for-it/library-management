package com.example.library.service.borrow;

import com.example.library.model.book.Book;
import com.example.library.model.borrow.BorrowRecordDto;
import com.example.library.model.borrow.BorrowRecord;
import com.example.library.model.borrow.BorrowStatus;
import com.example.library.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BorrowService {

    // Thêm phương thức mới
    BorrowRecord createBorrowRecord(Book book, User user, LocalDate borrowDate, LocalDate dueDate, BigDecimal fee);
    // Mượn sách (trả về bản ghi đã tạo)
    BorrowRecord borrowBook(int bookId, int userId);

    // Trả sách (trả về bản ghi đã cập nhật)
    BorrowRecord returnBook(int recordId);

    // Lịch sử mượn của một user
    List<BorrowRecord> getHistoryByUser(int userId);

    // Toàn bộ lịch sử (cho thủ thư)
    List<BorrowRecord> getAllHistory();

    // Danh sách đang quá hạn
    List<BorrowRecord> getOverdueRecords();

    // Tìm bản ghi theo id
    BorrowRecord findById(int id);

    public List<BorrowRecordDto> getOverdueRecordDtos();

    Page<BorrowRecordDto> getHistoryDtosByUser(int userId, Pageable pageable);
    Page<BorrowRecordDto> getAllHistoryDtos(Pageable pageable);

    Page<BorrowRecordDto> getOverdueRecordDtos(Pageable pageable);

    Page<BorrowRecordDto> getHistoryDtosByUser(int userId, String status, Pageable pageable);
    Page<BorrowRecordDto> getAllHistoryDtos(String status, Pageable pageable);
}