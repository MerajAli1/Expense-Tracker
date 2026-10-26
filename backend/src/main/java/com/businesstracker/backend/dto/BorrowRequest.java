package com.businesstracker.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class BorrowRequest {

    @Positive(message = "Borrowed amount must be greater than zero")
    private double amount;

    @NotBlank(message = "Purpose is required")
    private String purpose;

    public BorrowRequest() {
    }

    public BorrowRequest(double amount, String purpose) {
        this.amount = amount;
        this.purpose = purpose;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
