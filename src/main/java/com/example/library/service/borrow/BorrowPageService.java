package com.example.library.service.borrow;

import com.example.library.model.borrow.BorrowFormDto;
import com.example.library.model.borrow.BorrowRecordDto;
import com.example.library.model.borrow.ReturnConfirmDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BorrowPageService {
    BorrowFormDto prepareBorrowForm(int bookId);
    void processBorrow(int bookId, Integer userId);
    ReturnConfirmDto prepareReturnConfirm(int recordId);
    void processReturn(int recordId);
    Page<BorrowRecordDto> getHistory(int page, int size, String status);
    List<BorrowRecordDto> getOverdue();
    Page<BorrowRecordDto> getOverdue(int page, int size);
}