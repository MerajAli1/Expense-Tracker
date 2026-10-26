package com.businesstracker.backend;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "expenses")
public class BaseExpense {
    @Id
    private String expenseId;
    private String title;
    private double baseAmount;
    private String category; // e.g. marketing, rent, utility, food, travel, supplies
    private String description;
    private LocalDate date;
    private String paymentMethod; // e.g. Cash, Credit Card, Bank Transfer, Online
    private boolean taxable;
    private double taxPercentage; // e.g. 0.18 for 18%
    private double taxAmount;
    private double totalAmount;
    private LocalDateTime createdAt;

    public BaseExpense() {
        this.createdAt = LocalDateTime.now();
        this.date = LocalDate.now();
        this.paymentMethod = "Cash";
    }

    public BaseExpense(String expenseId, String title, double baseAmount, String category) {
        this();
        this.expenseId = expenseId;
        this.title = title;
        this.baseAmount = baseAmount;
        this.category = category;
        this.taxable = false;
        this.taxPercentage = 0.0;
        this.taxAmount = 0.0;
        this.totalAmount = baseAmount;
    }

    public BaseExpense(String expenseId, String title, double baseAmount, String category,
                       String description, LocalDate date, String paymentMethod,
                       boolean taxable, double taxPercentage) {
        this();
        this.expenseId = expenseId;
        this.title = title;
        this.baseAmount = baseAmount;
        this.category = category;
        this.description = description;
        this.date = (date != null) ? date : LocalDate.now();
        this.paymentMethod = (paymentMethod != null) ? paymentMethod : "Cash";
        this.taxable = taxable;
        this.taxPercentage = taxPercentage;
        recalculateTotals();
    }

    public void recalculateTotals() {
        if (this.taxable && this.taxPercentage > 0) {
            this.taxAmount = Math.round(this.baseAmount * this.taxPercentage * 100.0) / 100.0;
            this.totalAmount = Math.round((this.baseAmount + this.taxAmount) * 100.0) / 100.0;
        } else {
            this.taxAmount = 0.0;
            this.totalAmount = this.baseAmount;
        }
    }

    public double getFinalAmountWithTax() {
        recalculateTotals();
        return totalAmount;
    }

    // Getters and Setters
    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(double baseAmount) {
        this.baseAmount = baseAmount;
        recalculateTotals();
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isTaxable() {
        return taxable;
    }

    public void setTaxable(boolean taxable) {
        this.taxable = taxable;
        recalculateTotals();
    }

    public double getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(double taxPercentage) {
        this.taxPercentage = taxPercentage;
        recalculateTotals();
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void displayReport() {
        System.out.println("[" + category + "] " + title + " -> Cost: PKR " + totalAmount +
                (taxable ? " (Base: " + baseAmount + " + Tax: " + taxAmount + ")" : ""));
    }
}
