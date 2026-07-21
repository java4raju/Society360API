package com.society360.analytics.service;

import com.society360.complaint.repository.ComplaintRepository;
import com.society360.finance.entity.Transaction;
import com.society360.finance.repository.TransactionRepository;
import com.society360.resident.entity.Resident;
import com.society360.resident.repository.ResidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsService {

    private final ResidentRepository residentRepository;
    private final TransactionRepository transactionRepository;
    private final ComplaintRepository complaintRepository;

    public Map<String, Object> overview() {
        Map<String, Object> kpi = new HashMap<>();
        kpi.put("totalResidents", residentRepository.count());
        kpi.put("activeResidents", residentRepository.countByStatus(Resident.Status.ACTIVE));
        kpi.put("totalIncome", transactionRepository.sumByType(Transaction.Type.INCOME));
        kpi.put("totalExpense", transactionRepository.sumByType(Transaction.Type.EXPENSE));
        kpi.put("openComplaints", complaintRepository.countByStatus("Open"));
        return kpi;
    }

    public Object financeTrend() {
        return transactionRepository.monthlyTrend(LocalDate.now().minusMonths(11).withDayOfMonth(1));
    }

    public Object complaintsByCategory() {
        return complaintRepository.countByCategory();
    }

    public Object occupancy() {
        return residentRepository.countByOccupancy();
    }
}
