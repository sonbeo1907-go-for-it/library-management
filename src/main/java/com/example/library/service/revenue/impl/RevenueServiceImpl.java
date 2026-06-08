package com.example.library.service.revenue.impl;

import com.example.library.model.revenue.RevenueDto;
import com.example.library.model.revenue.RevenueSummaryDto;
import com.example.library.repository.borrow.BorrowRepository;
import com.example.library.repository.cart.CartRepository;
import com.example.library.service.revenue.RevenueService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RevenueServiceImpl implements RevenueService {

    private final CartRepository cartRepository;
    private final BorrowRepository borrowRepository;

    public RevenueServiceImpl(CartRepository cartRepository, BorrowRepository borrowRepository) {
        this.cartRepository = cartRepository;
        this.borrowRepository = borrowRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RevenueDto> getRevenueByMonth(int year, int month, Pageable pageable) {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.plusMonths(1).minusDays(1);
        LocalDateTime start = firstDay.atStartOfDay();
        LocalDateTime end = lastDay.atTime(LocalTime.MAX);

        // Định dạng tiền
        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

        // Lấy doanh thu phí mượn từ Cart
        List<Object[]> rentalData = cartRepository.findRentalRevenueByDateRange(start, end);
        Map<LocalDate, BigDecimal> rentalMap = new HashMap<>();
        for (Object[] row : rentalData) {
            LocalDate date = toLocalDate(row[0]);
            BigDecimal total = (BigDecimal) row[1];
            rentalMap.put(date, total);
        }

        // Lấy doanh thu phí phạt từ BorrowRecord
        List<Object[]> fineData = borrowRepository.findFineRevenueByDateRange(start, end);
        Map<LocalDate, BigDecimal> fineMap = new HashMap<>();
        for (Object[] row : fineData) {
            LocalDate date = toLocalDate(row[0]);
            BigDecimal total = (BigDecimal) row[1];
            fineMap.put(date, total);
        }

        // Tạo danh sách các ngày có doanh thu
        Set<LocalDate> allDates = new TreeSet<>();
        allDates.addAll(rentalMap.keySet());
        allDates.addAll(fineMap.keySet());

        List<RevenueDto> list = allDates.stream()
                .map(date -> {
                    BigDecimal rental = rentalMap.getOrDefault(date, BigDecimal.ZERO);
                    BigDecimal fine = fineMap.getOrDefault(date, BigDecimal.ZERO);
                    RevenueDto dto = new RevenueDto(date, rental, fine);
                    dto.setFormattedRentalRevenue(numberFormat.format(rental) + " VNĐ");
                    dto.setFormattedFineRevenue(numberFormat.format(fine) + " VNĐ");
                    dto.setFormattedTotalRevenue(numberFormat.format(dto.getTotalRevenue()) + " VNĐ");
                    return dto;
                })
                .collect(Collectors.toList());

        // Phân trang thủ công
        int startIdx = (int) pageable.getOffset();
        int endIdx = Math.min(startIdx + pageable.getPageSize(), list.size());
        List<RevenueDto> pageContent = list.subList(startIdx, endIdx);

        return new PageImpl<>(pageContent, pageable, list.size());
    }

    @Override
    @Transactional(readOnly = true)
    public RevenueSummaryDto getRevenueSummary(int year, int month) {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.plusMonths(1).minusDays(1);
        LocalDateTime start = firstDay.atStartOfDay();
        LocalDateTime end = lastDay.atTime(LocalTime.MAX);

        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

        List<Object[]> rentalData = cartRepository.findRentalRevenueByDateRange(start, end);
        BigDecimal totalRental = BigDecimal.ZERO;
        for (Object[] row : rentalData) {
            totalRental = totalRental.add((BigDecimal) row[1]);
        }

        List<Object[]> fineData = borrowRepository.findFineRevenueByDateRange(start, end);
        BigDecimal totalFine = BigDecimal.ZERO;
        for (Object[] row : fineData) {
            totalFine = totalFine.add((BigDecimal) row[1]);
        }

        BigDecimal total = totalRental.add(totalFine);
        RevenueSummaryDto summary = new RevenueSummaryDto();
        summary.setTotalRental(totalRental);
        summary.setTotalFine(totalFine);
        summary.setTotal(total);
        summary.setFormattedTotalRental(numberFormat.format(totalRental) + " VNĐ");
        summary.setFormattedTotalFine(numberFormat.format(totalFine) + " VNĐ");
        summary.setFormattedTotal(numberFormat.format(total) + " VNĐ");
        return summary;
    }

    // Helper ép kiểu ngày an toàn
    private LocalDate toLocalDate(Object obj) {
        if (obj instanceof java.sql.Date) {
            return ((java.sql.Date) obj).toLocalDate();
        } else if (obj instanceof LocalDate) {
            return (LocalDate) obj;
        }
        throw new RuntimeException("Unsupported date type: " + obj.getClass());
    }
}