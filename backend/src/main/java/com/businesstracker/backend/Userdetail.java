package com.businesstracker.backend;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "user_details")
public class Userdetail {
    @Id
    private String userId;
    private String username;
    private String email;
    private double businessBalance;
    private double savingsBalance;
    private String currency;
    private LocalDateTime updatedAt;

    public Userdetail() {
        this.userId = "DEFAULT_USER";
        this.username = "Admin";
        this.email = "admin@businesstracker.com";
        this.businessBalance = 100000.0;
        this.savingsBalance = 50000.0;
        this.currency = "PKR";
        this.updatedAt = LocalDateTime.now();
    }

    public Userdetail(String userId, String username, double businessBalance, double savingsBalance) {
        this();
        this.userId = (userId != null && !userId.isEmpty()) ? userId : "DEFAULT_USER";
        this.username = username;
        this.businessBalance = businessBalance;
        this.savingsBalance = savingsBalance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getBusinessBalance() {
        return businessBalance;
    }

    public void setBusinessBalance(double businessBalance) {
        if (businessBalance >= 0) {
            this.businessBalance = Math.round(businessBalance * 100.0) / 100.0;
            this.updatedAt = LocalDateTime.now();
        } else {
            System.out.println("Error: Business Balance can not be Negative!");
        }
    }

    public double getSavingsBalance() {
        return savingsBalance;
    }

    public double getSavingsBalanceValue() {
        return savingsBalance;
    }

    public void setSavingsBalance(double savingsBalance) {
        if (savingsBalance >= 0) {
            this.savingsBalance = Math.round(savingsBalance * 100.0) / 100.0;
            this.updatedAt = LocalDateTime.now();
        } else {
            System.out.println("Error: Savings Balance can not be Negative!");
        }
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
