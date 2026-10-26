package com.businesstracker.backend.controller;

import com.businesstracker.backend.BaseExpense;
import com.businesstracker.backend.dto.ApiResponse;
import com.businesstracker.backend.dto.ExpenseSummaryDTO;
import com.businesstracker.backend.service.ExpenseService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BaseExpense>>> getAllExpenses(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<BaseExpense> expenses = expenseService.getAllExpenses(category, search, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.ok("Expenses retrieved successfully", expenses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BaseExpense>> getExpenseById(@PathVariable String id) {
        BaseExpense expense = expenseService.getExpenseById(id);
        return ResponseEntity.ok(ApiResponse.ok("Expense found", expense));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BaseExpense>> createExpense(@RequestBody BaseExpense expense) {
        BaseExpense created = expenseService.createExpense(expense);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Expense created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BaseExpense>> updateExpense(
            @PathVariable String id,
            @RequestBody BaseExpense details) {
        BaseExpense updated = expenseService.updateExpense(id, details);
        return ResponseEntity.ok(ApiResponse.ok("Expense updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(@PathVariable String id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok(ApiResponse.ok("Expense deleted successfully", null));
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<ExpenseSummaryDTO>> getExpenseSummary() {
        ExpenseSummaryDTO summary = expenseService.getExpenseSummary();
        return ResponseEntity.ok(ApiResponse.ok("Expense summary generated", summary));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<String>>> getAllCategories() {
        List<String> categories = expenseService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.ok("Categories retrieved", categories));
    }
}
