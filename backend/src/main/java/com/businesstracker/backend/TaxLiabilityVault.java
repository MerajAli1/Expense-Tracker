package com.businesstracker.backend;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "tax_vault")
public class TaxLiabilityVault {
    @Id
    private String id;
    private double totalTaxOwed;
    private double businessUsableCash;
    private boolean isEmergencyFundLocked;
    private LocalDateTime lastUpdated;

    public TaxLiabilityVault() {
        this.id = "DEFAULT_VAULT";
        this.totalTaxOwed = 0.0;
        this.businessUsableCash = 0.0;
        this.isEmergencyFundLocked = false;
        this.lastUpdated = LocalDateTime.now();
    }

    public TaxLiabilityVault(String id, double totalTaxOwed, double businessUsableCash) {
        this.id = (id != null && !id.isEmpty()) ? id : "DEFAULT_VAULT";
        this.totalTaxOwed = Math.round(totalTaxOwed * 100.0) / 100.0;
        this.businessUsableCash = Math.round(businessUsableCash * 100.0) / 100.0;
        this.isEmergencyFundLocked = false;
        this.lastUpdated = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotalTaxOwed() {
        return totalTaxOwed;
    }

    public void setTotalTaxOwed(double totalTaxOwed) {
        this.totalTaxOwed = Math.round(totalTaxOwed * 100.0) / 100.0;
        this.lastUpdated = LocalDateTime.now();
    }

    public double getBusinessUsableCash() {
        return businessUsableCash;
    }

    public void setBusinessUsableCash(double businessUsableCash) {
        this.businessUsableCash = Math.round(businessUsableCash * 100.0) / 100.0;
        this.lastUpdated = LocalDateTime.now();
    }

    public boolean isEmergencyFundLocked() {
        return isEmergencyFundLocked;
    }

    public void setEmergencyFundLocked(boolean emergencyFundLocked) {
        isEmergencyFundLocked = emergencyFundLocked;
        this.lastUpdated = LocalDateTime.now();
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void processReceipt(double totalReceiptAmount, double gstpercentage) {
        double baseExpense = totalReceiptAmount / (1 + gstpercentage);
        double isolatedTax = totalReceiptAmount - baseExpense;

        baseExpense = Math.round(baseExpense * 100.0) / 100.0;
        isolatedTax = Math.round(isolatedTax * 100.0) / 100.0;

        this.businessUsableCash = Math.round((this.businessUsableCash - baseExpense) * 100.0) / 100.0;
        this.totalTaxOwed = Math.round((this.totalTaxOwed + isolatedTax) * 100.0) / 100.0;
        this.lastUpdated = LocalDateTime.now();

        System.out.println("\n[OCR AI Engine Activated]");
        System.out.println("Extracted Total: PKR " + totalReceiptAmount);
        System.out.println("Parsed Base Expense: PKR " + baseExpense);
        System.out.println("Isolated Tax Liability: PKR " + isolatedTax);
    }

    public void lockEmergencyFund() {
        this.isEmergencyFundLocked = true;
        this.lastUpdated = LocalDateTime.now();
        System.out.println("🚨 CRITICAL STATE: Emergency Fund is now Locked until full repayment status update.");
    }

    public void unlockEmergencyFund() {
        this.isEmergencyFundLocked = false;
        this.lastUpdated = LocalDateTime.now();
        System.out.println("✅ Emergency Fund has been unlocked.");
    }
}