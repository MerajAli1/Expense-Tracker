package com.businesstracker.backend.service;

import com.businesstracker.backend.BaseExpense;
import com.businesstracker.backend.ExpenseRepository;
import com.businesstracker.backend.dto.ExpenseSummaryDTO;
import com.businesstracker.backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<BaseExpense> getAllExpenses(String category, String search, LocalDate startDate, LocalDate endDate) {
        List<BaseExpense> expenses;

        if (category != null && !category.trim().isEmpty() && startDate != null && endDate != null) {
            expenses = expenseRepository.findByCategoryIgnoreCaseAndDateBetweenOrderByDateDesc(category.trim(), startDate, endDate);
        } else if (category != null && !category.trim().isEmpty()) {
            expenses = expenseRepository.findByCategoryIgnoreCaseOrderByDateDesc(category.trim());
        } else if (startDate != null && endDate != null) {
            expenses = expenseRepository.findByDateBetweenOrderByDateDesc(startDate, endDate);
        } else {
            expenses = expenseRepository.findAllByOrderByDateDesc();
        }

        if (search != null && !search.trim().isEmpty()) {
            String lowerSearch = search.trim().toLowerCase();
            expenses = expenses.stream()
                    .filter(e -> (e.getTitle() != null && e.getTitle().toLowerCase().contains(lowerSearch)) ||
                            (e.getDescription() != null && e.getDescription().toLowerCase().contains(lowerSearch)))
                    .collect(Collectors.toList());
        }

        return expenses;
    }

    public BaseExpense getExpenseById(String id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with ID: " + id));
    }

    public BaseExpense createExpense(BaseExpense expense) {
        if (expense.getDate() == null) {
            expense.setDate(LocalDate.now());
        }
        if (expense.getPaymentMethod() == null || expense.getPaymentMethod().trim().isEmpty()) {
            expense.setPaymentMethod("Cash");
        }
        expense.recalculateTotals();
        return expenseRepository.save(expense);
    }

    public BaseExpense updateExpense(String id, BaseExpense details) {
        BaseExpense existing = getExpenseById(id);

        existing.setTitle(details.getTitle());
        existing.setBaseAmount(details.getBaseAmount());
        existing.setCategory(details.getCategory());
        existing.setDescription(details.getDescription());
        if (details.getDate() != null) {
            existing.setDate(details.getDate());
        }
        if (details.getPaymentMethod() != null) {
            existing.setPaymentMethod(details.getPaymentMethod());
        }
        existing.setTaxable(details.isTaxable());
        existing.setTaxPercentage(details.getTaxPercentage());
        existing.recalculateTotals();

        return expenseRepository.save(existing);
    }

    public void deleteExpense(String id) {
        BaseExpense existing = getExpenseById(id);
        expenseRepository.delete(existing);
    }

    public ExpenseSummaryDTO getExpenseSummary() {
        List<BaseExpense> all = expenseRepository.findAllByOrderByDateDesc();

        double totalExpenses = 0.0;
        double totalBase = 0.0;
        double totalTax = 0.0;
        Map<String, Double> categoryMap = new LinkedHashMap<>();
        Map<String, Double> monthlyMap = new TreeMap<>();

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (BaseExpense e : all) {
            double finalAmount = (e.getTotalAmount() > 0) ? e.getTotalAmount() : e.getBaseAmount();
            totalExpenses += finalAmount;
            totalBase += e.getBaseAmount();
            totalTax += e.getTaxAmount();

            String cat = (e.getCategory() != null && !e.getCategory().trim().isEmpty())
                    ? e.getCategory().trim()
                    : "Uncategorized";
            categoryMap.put(cat, Math.round((categoryMap.getOrDefault(cat, 0.0) + finalAmount) * 100.0) / 100.0);

            if (e.getDate() != null) {
                String monthKey = e.getDate().format(monthFormatter);
                monthlyMap.put(monthKey, Math.round((monthlyMap.getOrDefault(monthKey, 0.0) + finalAmount) * 100.0) / 100.0);
            }
        }

        List<BaseExpense> recent = all.stream().limit(5).collect(Collectors.toList());

        return new ExpenseSummaryDTO(
                totalExpenses,
                totalBase,
                totalTax,
                all.size(),
                categoryMap,
                monthlyMap,
                recent
        );
    }

    public List<String> getAllCategories() {
        List<BaseExpense> all = expenseRepository.findAll();
        Set<String> categories = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        // Default standard categories
        categories.add("Rent");
        categories.add("Utility");
        categories.add("Marketing");
        categories.add("Salaries");
        categories.add("Supplies");
        categories.add("Travel");
        categories.add("Maintenance");
        categories.add("Food & Entertainment");

        for (BaseExpense e : all) {
            if (e.getCategory() != null && !e.getCategory().trim().isEmpty()) {
                categories.add(e.getCategory().trim());
            }
        }
        return new ArrayList<>(categories);
    }
}
