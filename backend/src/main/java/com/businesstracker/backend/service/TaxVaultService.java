package com.businesstracker.backend.service;

import com.businesstracker.backend.BaseExpense;
import com.businesstracker.backend.ExpenseRepository;
import com.businesstracker.backend.TaxLiabilityVault;
import com.businesstracker.backend.TaxVaultRepository;
import com.businesstracker.backend.dto.ReceiptProcessRequest;
import com.businesstracker.backend.dto.ReceiptProcessResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TaxVaultService {

    private final TaxVaultRepository taxVaultRepository;
    private final ExpenseRepository expenseRepository;

    private static final String DEFAULT_VAULT_ID = "DEFAULT_VAULT";

    public TaxVaultService(TaxVaultRepository taxVaultRepository, ExpenseRepository expenseRepository) {
        this.taxVaultRepository = taxVaultRepository;
        this.expenseRepository = expenseRepository;
    }

    public TaxLiabilityVault getOrCreateVault() {
        return taxVaultRepository.findById(DEFAULT_VAULT_ID)
                .orElseGet(() -> {
                    TaxLiabilityVault newVault = new TaxLiabilityVault(DEFAULT_VAULT_ID, 0.0, 100000.0);
                    return taxVaultRepository.save(newVault);
                });
    }

    public ReceiptProcessResponse processReceipt(ReceiptProcessRequest request) {
        TaxLiabilityVault vault = getOrCreateVault();

        double totalReceiptAmount = request.getReceiptAmount();
        double gstRate = request.getGstPercentage();

        // Calculate base and tax separation
        double baseExpense = (gstRate > 0)
                ? (totalReceiptAmount / (1 + gstRate))
                : totalReceiptAmount;
        double isolatedTax = totalReceiptAmount - baseExpense;

        baseExpense = Math.round(baseExpense * 100.0) / 100.0;
        isolatedTax = Math.round(isolatedTax * 100.0) / 100.0;

        // Apply vault updates
        vault.setBusinessUsableCash(vault.getBusinessUsableCash() - baseExpense);
        vault.setTotalTaxOwed(vault.getTotalTaxOwed() + isolatedTax);
        vault.setLastUpdated(LocalDateTime.now());
        vault = taxVaultRepository.save(vault);

        // Automatically record this expense in the database
        BaseExpense expense = new BaseExpense();
        expense.setTitle(request.getTitle());
        expense.setCategory((request.getCategory() != null && !request.getCategory().trim().isEmpty())
                ? request.getCategory() : "General");
        expense.setDescription(request.getDescription());
        expense.setBaseAmount(baseExpense);
        expense.setTaxable(gstRate > 0);
        expense.setTaxPercentage(gstRate);
        expense.setTaxAmount(isolatedTax);
        expense.setTotalAmount(totalReceiptAmount);
        expense.setDate(LocalDate.now());
        expense.setPaymentMethod((request.getPaymentMethod() != null) ? request.getPaymentMethod() : "Cash");

        BaseExpense savedExpense = expenseRepository.save(expense);

        return new ReceiptProcessResponse(
                totalReceiptAmount,
                baseExpense,
                isolatedTax,
                gstRate,
                vault.getBusinessUsableCash(),
                vault.getTotalTaxOwed(),
                vault.isEmergencyFundLocked(),
                savedExpense
        );
    }

    public TaxLiabilityVault updateUsableCash(double cash) {
        TaxLiabilityVault vault = getOrCreateVault();
        vault.setBusinessUsableCash(cash);
        return taxVaultRepository.save(vault);
    }

    public TaxLiabilityVault toggleEmergencyLock(boolean lock) {
        TaxLiabilityVault vault = getOrCreateVault();
        if (lock) {
            vault.lockEmergencyFund();
        } else {
            vault.unlockEmergencyFund();
        }
        return taxVaultRepository.save(vault);
    }
}
