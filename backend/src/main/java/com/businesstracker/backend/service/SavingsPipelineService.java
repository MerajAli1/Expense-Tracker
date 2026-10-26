package com.businesstracker.backend.service;

import com.businesstracker.backend.SavingsPipelineRepository;
import com.businesstracker.backend.SavingsPipelines;
import com.businesstracker.backend.Userdetail;
import com.businesstracker.backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class SavingsPipelineService {

    private final SavingsPipelineRepository savingsPipelineRepository;
    private final UserDetailService userDetailService;

    public SavingsPipelineService(SavingsPipelineRepository savingsPipelineRepository,
                                  UserDetailService userDetailService) {
        this.savingsPipelineRepository = savingsPipelineRepository;
        this.userDetailService = userDetailService;
    }

    public List<SavingsPipelines> getAllPipelines(Boolean isReturned) {
        if (isReturned != null) {
            return savingsPipelineRepository.findByIsReturnedOrderByDateBorrowedDesc(isReturned);
        }
        return savingsPipelineRepository.findAllByOrderByDateBorrowedDesc();
    }

    public SavingsPipelines getById(String transactionId) {
        return savingsPipelineRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Savings transaction not found with ID: " + transactionId));
    }

    public SavingsPipelines borrowMoney(double amount, String purpose) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Borrowed amount must be greater than zero.");
        }

        String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        SavingsPipelines record = new SavingsPipelines(transactionId, purpose, amount, LocalDate.now());

        // Update User balances: deduct from savings, inject into business balance
        Userdetail user = userDetailService.getOrCreateUser();
        if (user.getSavingsBalance() < amount) {
            throw new IllegalArgumentException("Insufficient savings balance! Available: " + user.getSavingsBalance());
        }
        user.setSavingsBalance(user.getSavingsBalance() - amount);
        user.setBusinessBalance(user.getBusinessBalance() + amount);
        userDetailService.save(user);

        return savingsPipelineRepository.save(record);
    }

    public SavingsPipelines returnMoney(String transactionId) {
        SavingsPipelines record = getById(transactionId);
        if (record.isReturned()) {
            throw new IllegalArgumentException("Transaction " + transactionId + " is already marked as returned.");
        }

        record.returnMoney();

        // Replenish user savings balance from business balance
        Userdetail user = userDetailService.getOrCreateUser();
        if (user.getBusinessBalance() >= record.getBorrowedAmount()) {
            user.setBusinessBalance(user.getBusinessBalance() - record.getBorrowedAmount());
            user.setSavingsBalance(user.getSavingsBalance() + record.getBorrowedAmount());
            userDetailService.save(user);
        }

        return savingsPipelineRepository.save(record);
    }
}
