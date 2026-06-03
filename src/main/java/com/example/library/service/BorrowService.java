package com.example.library.service;

import com.example.library.dto.BorrowRecordDto;
import com.example.library.model.BorrowRecord;

import java.util.List;

public interface BorrowService {

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

    List<BorrowRecordDto> getHistoryDtosByUser(int userId);

    List<BorrowRecordDto> getAllHistoryDtos();
}