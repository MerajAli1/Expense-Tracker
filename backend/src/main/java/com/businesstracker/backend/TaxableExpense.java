package com.businesstracker.backend;

import java.time.LocalDate;

public class TaxableExpense extends BaseExpense {

    public TaxableExpense() {
        super();
        setTaxable(true);
    }

    public TaxableExpense(String expenseId, String title, double baseAmount, String category, double taxpercentage) {
        super(expenseId, title, baseAmount, category, null, LocalDate.now(), "Cash", true, taxpercentage);
    }

    public TaxableExpense(String expenseId, String title, double baseAmount, String category,
                          String description, LocalDate date, String paymentMethod, double taxpercentage) {
        super(expenseId, title, baseAmount, category, description, date, paymentMethod, true, taxpercentage);
    }

    public double getTaxpercentage() {
        return getTaxPercentage();
    }

    public void setTaxpercentage(double taxpercentage) {
        setTaxPercentage(taxpercentage);
    }

    @Override
    public double getFinalAmountWithTax() {
        return super.getFinalAmountWithTax();
    }

    @Override
    public void displayReport() {
        System.out.println("[" + getCategory() + " - TAXABLE] " + getTitle() +
                " -> Base: PKR " + getBaseAmount() + " | Total With Tax: PKR " + getFinalAmountWithTax());
    }
}
