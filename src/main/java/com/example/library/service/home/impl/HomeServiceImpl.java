package com.example.library.service.home.impl;

import com.example.library.model.borrow.BorrowStatus;
import com.example.library.model.home.HomeDto;
import com.example.library.model.user.Role;
import com.example.library.model.user.User;
import com.example.library.service.borrow.BorrowService;
import com.example.library.service.home.HomeService;
import com.example.library.service.statistics.BookStatisticsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class HomeServiceImpl implements HomeService {

    private final BookStatisticsService statisticsService;
    private final BorrowService borrowService;

    public HomeServiceImpl(BookStatisticsService statisticsService, BorrowService borrowService) {
        this.statisticsService = statisticsService;
        this.borrowService = borrowService;
    }

    @Override
    public HomeDto getHomeData(User currentUser) {
        HomeDto dto = new HomeDto();
        dto.setUser(currentUser);
        boolean acceptedRoles = currentUser.getRole() == Role.LIBRARIAN
                || currentUser.getRole() == Role.ADMIN;
        dto.setLibrarian(acceptedRoles);

        if (acceptedRoles) {
            dto.setTotalBooks(statisticsService.countTotalBooks());
            dto.setAvailableBooks(statisticsService.countAvailableBooks());
            dto.setBorrowedBooks(statisticsService.countBorrowedBooks());
            dto.setOverdueBooks(statisticsService.countOverdueBooks());
            dto.setTotalUsers(statisticsService.countReaders());
        } else {
            var history = borrowService.getHistoryByUser(currentUser.getId());
            long currentlyBorrowed = history.stream()
                    .filter(r -> r.getStatus() == BorrowStatus.BORROWED)
                    .count();
            long overdue = history.stream()
                    .filter(r -> r.getStatus() == BorrowStatus.BORROWED && r.getDueDate().isBefore(LocalDate.now()))
                    .count();
            dto.setCurrentlyBorrowed(currentlyBorrowed);
            dto.setOverdue(overdue);
        }
        return dto;
    }
}