package com.businesstracker.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class ReceiptProcessRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @Positive(message = "Receipt amount must be positive")
    private double receiptAmount;

    @PositiveOrZero(message = "GST percentage cannot be negative")
    private double gstPercentage; // e.g. 0.18 for 18%

    private String category; // e.g. "Utility", "Supplies", "Marketing", "Travel"
    private String description;
    private String paymentMethod;

    public ReceiptProcessRequest() {
        this.category = "General";
        this.paymentMethod = "Cash";
    }

    public ReceiptProcessRequest(String title, double receiptAmount, double gstPercentage, String category, String description) {
        this();
        this.title = title;
        this.receiptAmount = receiptAmount;
        this.gstPercentage = gstPercentage;
        this.category = category;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(double receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public double getGstPercentage() {
        return gstPercentage;
    }

    public void setGstPercentage(double gstPercentage) {
        this.gstPercentage = gstPercentage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
