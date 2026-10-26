package com.businesstracker.backend.dto;

import jakarta.validation.constraints.PositiveOrZero;

public class BalanceUpdateRequest {

    @PositiveOrZero(message = "Business balance cannot be negative")
    private double businessBalance;

    @PositiveOrZero(message = "Savings balance cannot be negative")
    private double savingsBalance;

    public BalanceUpdateRequest() {
    }

    public BalanceUpdateRequest(double businessBalance, double savingsBalance) {
        this.businessBalance = businessBalance;
        this.savingsBalance = savingsBalance;
    }

    public double getBusinessBalance() {
        return businessBalance;
    }

    public void setBusinessBalance(double businessBalance) {
        this.businessBalance = businessBalance;
    }

    public double getSavingsBalance() {
        return savingsBalance;
    }

    public void setSavingsBalance(double savingsBalance) {
        this.savingsBalance = savingsBalance;
    }
}
