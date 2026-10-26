package com.businesstracker.backend.dto;

import com.businesstracker.backend.BaseExpense;

import java.util.List;
import java.util.Map;

public class ExpenseSummaryDTO {
    private double totalExpenses;
    private double totalBaseAmount;
    private double totalTaxAmount;
    private int transactionCount;
    private Map<String, Double> categoryBreakdown;
    private Map<String, Double> monthlyBreakdown;
    private List<BaseExpense> recentExpenses;

    public ExpenseSummaryDTO() {
    }

    public ExpenseSummaryDTO(double totalExpenses, double totalBaseAmount, double totalTaxAmount,
                             int transactionCount, Map<String, Double> categoryBreakdown,
                             Map<String, Double> monthlyBreakdown, List<BaseExpense> recentExpenses) {
        this.totalExpenses = Math.round(totalExpenses * 100.0) / 100.0;
        this.totalBaseAmount = Math.round(totalBaseAmount * 100.0) / 100.0;
        this.totalTaxAmount = Math.round(totalTaxAmount * 100.0) / 100.0;
        this.transactionCount = transactionCount;
        this.categoryBreakdown = categoryBreakdown;
        this.monthlyBreakdown = monthlyBreakdown;
        this.recentExpenses = recentExpenses;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public double getTotalBaseAmount() {
        return totalBaseAmount;
    }

    public void setTotalBaseAmount(double totalBaseAmount) {
        this.totalBaseAmount = totalBaseAmount;
    }

    public double getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(double totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }

    public Map<String, Double> getCategoryBreakdown() {
        return categoryBreakdown;
    }

    public void setCategoryBreakdown(Map<String, Double> categoryBreakdown) {
        this.categoryBreakdown = categoryBreakdown;
    }

    public Map<String, Double> getMonthlyBreakdown() {
        return monthlyBreakdown;
    }

    public void setMonthlyBreakdown(Map<String, Double> monthlyBreakdown) {
        this.monthlyBreakdown = monthlyBreakdown;
    }

    public List<BaseExpense> getRecentExpenses() {
        return recentExpenses;
    }

    public void setRecentExpenses(List<BaseExpense> recentExpenses) {
        this.recentExpenses = recentExpenses;
    }
}
