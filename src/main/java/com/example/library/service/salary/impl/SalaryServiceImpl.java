package com.example.library.service.salary.impl;

import com.example.library.model.salary.*;
import com.example.library.model.user.Role;
import com.example.library.model.user.User;
import com.example.library.repository.borrow.BorrowRepository;
import com.example.library.repository.cart.CartRepository;
import com.example.library.repository.salary.SalaryConfigRepository;
import com.example.library.repository.salary.SalaryRecordRepository;
import com.example.library.repository.user.UserRepository;
import com.example.library.service.salary.SalaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SalaryServiceImpl implements SalaryService {

    private final SalaryConfigRepository configRepo;
    private final SalaryRecordRepository recordRepo;
    private final UserRepository userRepo;
    private final CartRepository cartRepo;
    private final BorrowRepository borrowRepo;
    private static final NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));


    public SalaryServiceImpl(SalaryConfigRepository configRepo, SalaryRecordRepository recordRepo,
                             UserRepository userRepo, CartRepository cartRepo, BorrowRepository borrowRepo) {
        this.configRepo = configRepo;
        this.recordRepo = recordRepo;
        this.userRepo = userRepo;
        this.cartRepo = cartRepo;
        this.borrowRepo = borrowRepo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void calculateMonthlySalary(int year, int month) {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.plusMonths(1).minusDays(1);
        LocalDateTime startDateTime = firstDay.atStartOfDay();
        LocalDateTime endDateTime = lastDay.atTime(LocalTime.MAX);

        List<User> librarians = userRepo.findByRoleAndIsDeletedFalse(Role.LIBRARIAN);

        for (User librarian : librarians) {
            SalaryConfig config = configRepo.findByUserId(librarian.getId()).orElse(null);
            if (config == null) continue; // Bỏ qua nếu chưa có cấu hình

            long approvalCount = cartRepo.countApprovalsByUser(librarian.getId(), startDateTime, endDateTime);
            BigDecimal totalFee = cartRepo.sumTotalFeeByUser(librarian.getId(), startDateTime, endDateTime);
            long returnCount = borrowRepo.countReturnsByUser(librarian.getId(), firstDay, lastDay);

            BigDecimal commission = BigDecimal.ZERO;
            if (config.getCommissionRate().compareTo(BigDecimal.ZERO) > 0) {
                commission = totalFee.multiply(config.getCommissionRate());
            }

            BigDecimal bonus = BigDecimal.ZERO;
            if (config.getBonusPerApproval().compareTo(BigDecimal.ZERO) > 0) {
                bonus = bonus.add(config.getBonusPerApproval().multiply(BigDecimal.valueOf(approvalCount)));
            }
            if (config.getBonusPerReturn().compareTo(BigDecimal.ZERO) > 0) {
                bonus = bonus.add(config.getBonusPerReturn().multiply(BigDecimal.valueOf(returnCount)));
            }

            BigDecimal totalSalary = config.getBaseSalary().add(commission).add(bonus);

            SalaryRecord record = recordRepo.findByUserIdAndMonthAndYear(librarian.getId(), month, year)
                    .orElse(new SalaryRecord(librarian, month, year));
            record.setBaseSalary(config.getBaseSalary());
            record.setCommissionAmount(commission);
            record.setBonusAmount(bonus);
            record.setTotalSalary(totalSalary);
            record.setStatus(SalaryStatus.PENDING);
            recordRepo.save(record);
        }
    }

    private SalaryRecordDto toDto(SalaryRecord record) {
        SalaryRecordDto dto = new SalaryRecordDto();
        dto.setId(record.getId());
        dto.setUserId(record.getUser().getId());
        dto.setFullName(record.getUser().getFullName()); // user đã được load trong transaction
        dto.setBaseSalary(record.getBaseSalary());
        dto.setCommissionAmount(record.getCommissionAmount());
        dto.setBonusAmount(record.getBonusAmount());
        dto.setTotalSalary(record.getTotalSalary());
        dto.setStatus(record.getStatus().name());
        dto.setFormattedBaseSalary(format(record.getBaseSalary()));
        dto.setFormattedCommissionAmount(format(record.getCommissionAmount()));
        dto.setFormattedBonusAmount(format(record.getBonusAmount()));
        dto.setFormattedTotalSalary(format(record.getTotalSalary()));
        return dto;
    }

    private String format(BigDecimal amount) {
        if (amount == null) return "0 VNĐ";
        return numberFormat.format(amount) + " VNĐ";
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void paySalary(int recordId) {
        SalaryRecord record = recordRepo.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi lương"));
        if (record.getStatus() != SalaryStatus.PENDING) {
            throw new RuntimeException("Chỉ có thể thanh toán lương đang chờ.");
        }
        record.setStatus(SalaryStatus.PAID);
        recordRepo.save(record);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cancelSalary(int recordId) {
        SalaryRecord record = recordRepo.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi lương"));
        if (record.getStatus() != SalaryStatus.PENDING) {
            throw new RuntimeException("Chỉ có thể hủy lương đang chờ.");
        }
        record.setStatus(SalaryStatus.CANCELLED);
        recordRepo.save(record);
    }

    @Override
    public SalaryConfig getSalaryConfig(int userId) {
        return configRepo.findByUserId(userId).orElse(new SalaryConfig());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveSalaryConfig(SalaryConfig config) {
        SalaryConfig existing = configRepo.findByUserId(config.getUser().getId()).orElse(null);
        if (existing != null) {
            existing.setBaseSalary(config.getBaseSalary());
            existing.setCommissionRate(config.getCommissionRate());
            existing.setBonusPerApproval(config.getBonusPerApproval());
            existing.setBonusPerReturn(config.getBonusPerReturn());
            configRepo.save(existing);
        } else {
            configRepo.save(config);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SalaryConfigDto getSalaryConfigDto(int userId) {
        SalaryConfig config = configRepo.findByUserId(userId).orElse(null);
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        SalaryConfigDto dto = new SalaryConfigDto();
        dto.setUserId(userId);
        dto.setFullName(user.getFullName());
        if (config != null) {
            dto.setBaseSalary(config.getBaseSalary());
            dto.setCommissionRate(config.getCommissionRate());
            dto.setBonusPerApproval(config.getBonusPerApproval());
            dto.setBonusPerReturn(config.getBonusPerReturn());
        } else {
            dto.setBaseSalary(BigDecimal.ZERO);
            dto.setCommissionRate(BigDecimal.ZERO);
            dto.setBonusPerApproval(BigDecimal.ZERO);
            dto.setBonusPerReturn(BigDecimal.ZERO);
        }
        return dto;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveSalaryConfigDto(SalaryConfigDto dto) {
        SalaryConfig config = configRepo.findByUserId(dto.getUserId()).orElse(new SalaryConfig());
        User user = userRepo.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        config.setUser(user);
        config.setBaseSalary(dto.getBaseSalary());
        config.setCommissionRate(dto.getCommissionRate());
        config.setBonusPerApproval(dto.getBonusPerApproval());
        config.setBonusPerReturn(dto.getBonusPerReturn());
        configRepo.save(config);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibrarianSalaryDto> getLibrarianSalaries(int year, int month, String search) {
        List<User> librarians = userRepo.findByRoleAndIsDeletedFalse(Role.LIBRARIAN);
        List<SalaryRecord> records = recordRepo.findByMonthAndYear(month, year);

        Map<Integer, SalaryRecord> recordMap = records.stream()
                .collect(Collectors.toMap(r -> r.getUser().getId(), r -> r));

        List<LibrarianSalaryDto> result = new ArrayList<>();
        for (User lib : librarians) {
            // Lọc theo tên nếu có search
            if (search != null && !search.isBlank()) {
                String lowerSearch = search.toLowerCase().trim();
                if (!lib.getFullName().toLowerCase().contains(lowerSearch)) {
                    continue;
                }
            }

            LibrarianSalaryDto dto = new LibrarianSalaryDto();
            dto.setUserId(lib.getId());
            dto.setFullName(lib.getFullName());

            SalaryConfig config = configRepo.findByUserId(lib.getId()).orElse(null);
            dto.setHasConfig(config != null);

            SalaryRecord record = recordMap.get(lib.getId());
            if (record != null) {
                dto.setHasSalaryRecord(true);
                dto.setSalaryRecord(toDto(record));
            } else {
                dto.setHasSalaryRecord(false);
                dto.setSalaryRecord(null);
            }
            result.add(dto);
        }
        return result;
    }
}