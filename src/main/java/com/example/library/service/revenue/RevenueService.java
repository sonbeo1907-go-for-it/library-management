package com.example.library.service.revenue;

import com.example.library.model.revenue.RevenueDto;
import com.example.library.model.revenue.RevenueSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RevenueService {
    Page<RevenueDto> getRevenueByMonth(int year, int month, Pageable pageable);
    RevenueSummaryDto getRevenueSummary(int year, int month);
}