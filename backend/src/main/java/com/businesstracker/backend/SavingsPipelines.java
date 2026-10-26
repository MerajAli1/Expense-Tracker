package com.businesstracker.backend;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "savings_pipeline")
public class SavingsPipelines {
    @Id
    private String transactionId;
    private String purpose;
    private double borrowedAmount;
    private LocalDate dateBorrowed;
    private LocalDate dateReturned;
    private boolean isReturned;
    private LocalDateTime createdAt;

    public SavingsPipelines() {
        this.createdAt = LocalDateTime.now();
        this.dateBorrowed = LocalDate.now();
        this.isReturned = false;
        this.purpose = "Operational Expense";
    }

    public SavingsPipelines(String transactionId, double borrowedAmount, String datestr) {
        this();
        this.transactionId = transactionId;
        this.borrowedAmount = Math.round(borrowedAmount * 100.0) / 100.0;
        this.dateBorrowed = LocalDate.parse(datestr);
        this.isReturned = false;
    }

    public SavingsPipelines(String transactionId, String purpose, double borrowedAmount, LocalDate dateBorrowed) {
        this();
        this.transactionId = transactionId;
        this.purpose = purpose;
        this.borrowedAmount = Math.round(borrowedAmount * 100.0) / 100.0;
        this.dateBorrowed = (dateBorrowed != null) ? dateBorrowed : LocalDate.now();
        this.isReturned = false;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public double getBorrowedAmount() {
        return borrowedAmount;
    }

    public void setBorrowedAmount(double borrowedAmount) {
        this.borrowedAmount = Math.round(borrowedAmount * 100.0) / 100.0;
    }

    public LocalDate getDateBorrowed() {
        return dateBorrowed;
    }

    public void setDateBorrowed(LocalDate dateBorrowed) {
        this.dateBorrowed = dateBorrowed;
    }

    public LocalDate getDateReturned() {
        return dateReturned;
    }

    public void setDateReturned(LocalDate dateReturned) {
        this.dateReturned = dateReturned;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void checkStatus() {
        if (isReturned) {
            System.out.println("✅ Transaction " + transactionId + ": PKR " + borrowedAmount + " has been successfully returned to Savings.");
        } else {
            System.out.println("⚠️ Alert: PKR " + borrowedAmount + " was borrowed on " + dateBorrowed + " and is Still OUTSTANDING!");
        }
    }

    public void returnMoney() {
        this.isReturned = true;
        this.dateReturned = LocalDate.now();
        System.out.println("✨ Success: PKR " + borrowedAmount + " returned back to Savings Fund.");
    }
}
