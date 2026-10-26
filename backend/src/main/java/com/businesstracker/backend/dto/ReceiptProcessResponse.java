package com.businesstracker.backend.dto;

import com.businesstracker.backend.BaseExpense;

public class ReceiptProcessResponse {
    private double receiptAmount;
    private double baseExpense;
    private double isolatedTax;
    private double gstPercentage;
    private double remainingUsableCash;
    private double currentTotalTaxOwed;
    private boolean emergencyFundLocked;
    private BaseExpense createdExpense;

    public ReceiptProcessResponse() {
    }

    public ReceiptProcessResponse(double receiptAmount, double baseExpense, double isolatedTax,
                                  double gstPercentage, double remainingUsableCash,
                                  double currentTotalTaxOwed, boolean emergencyFundLocked,
                                  BaseExpense createdExpense) {
        this.receiptAmount = receiptAmount;
        this.baseExpense = baseExpense;
        this.isolatedTax = isolatedTax;
        this.gstPercentage = gstPercentage;
        this.remainingUsableCash = remainingUsableCash;
        this.currentTotalTaxOwed = currentTotalTaxOwed;
        this.emergencyFundLocked = emergencyFundLocked;
        this.createdExpense = createdExpense;
    }

    public double getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(double receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public double getBaseExpense() {
        return baseExpense;
    }

    public void setBaseExpense(double baseExpense) {
        this.baseExpense = baseExpense;
    }

    public double getIsolatedTax() {
        return isolatedTax;
    }

    public void setIsolatedTax(double isolatedTax) {
        this.isolatedTax = isolatedTax;
    }

    public double getGstPercentage() {
        return gstPercentage;
    }

    public void setGstPercentage(double gstPercentage) {
        this.gstPercentage = gstPercentage;
    }

    public double getRemainingUsableCash() {
        return remainingUsableCash;
    }

    public void setRemainingUsableCash(double remainingUsableCash) {
        this.remainingUsableCash = remainingUsableCash;
    }

    public double getCurrentTotalTaxOwed() {
        return currentTotalTaxOwed;
    }

    public void setCurrentTotalTaxOwed(double currentTotalTaxOwed) {
        this.currentTotalTaxOwed = currentTotalTaxOwed;
    }

    public boolean isEmergencyFundLocked() {
        return emergencyFundLocked;
    }

    public void setEmergencyFundLocked(boolean emergencyFundLocked) {
        this.emergencyFundLocked = emergencyFundLocked;
    }

    public BaseExpense getCreatedExpense() {
        return createdExpense;
    }

    public void setCreatedExpense(BaseExpense createdExpense) {
        this.createdExpense = createdExpense;
    }
}
