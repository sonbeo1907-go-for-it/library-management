package com.example.library.service.borrow;

import com.example.library.model.borrow.BorrowFormDto;
import com.example.library.model.borrow.BorrowRecordDto;
import com.example.library.model.borrow.ReturnConfirmDto;
import java.util.List;

public interface BorrowPageService {
    BorrowFormDto prepareBorrowForm(int bookId);
    void processBorrow(int bookId, Integer userId);
    ReturnConfirmDto prepareReturnConfirm(int recordId);
    void processReturn(int recordId);
    List<BorrowRecordDto> getHistory();
    List<BorrowRecordDto> getOverdue();
}