package com.businesstracker.backend.controller;

import com.businesstracker.backend.SavingsPipelines;
import com.businesstracker.backend.dto.ApiResponse;
import com.businesstracker.backend.dto.BorrowRequest;
import com.businesstracker.backend.service.SavingsPipelineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/savings")
@CrossOrigin(origins = "*")
public class SavingsPipelineController {

    private final SavingsPipelineService savingsPipelineService;

    public SavingsPipelineController(SavingsPipelineService savingsPipelineService) {
        this.savingsPipelineService = savingsPipelineService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SavingsPipelines>>> getAllTransactions(
            @RequestParam(required = false) Boolean isReturned) {
        List<SavingsPipelines> list = savingsPipelineService.getAllPipelines(isReturned);
        return ResponseEntity.ok(ApiResponse.ok("Savings transactions retrieved", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SavingsPipelines>> getTransactionById(@PathVariable String id) {
        SavingsPipelines record = savingsPipelineService.getById(id);
        return ResponseEntity.ok(ApiResponse.ok("Savings transaction found", record));
    }

    @PostMapping("/borrow")
    public ResponseEntity<ApiResponse<SavingsPipelines>> borrowFromSavings(
            @Valid @RequestBody BorrowRequest request) {
        SavingsPipelines record = savingsPipelineService.borrowMoney(request.getAmount(), request.getPurpose());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Funds borrowed from savings successfully", record));
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<ApiResponse<SavingsPipelines>> returnToSavings(@PathVariable String id) {
        SavingsPipelines record = savingsPipelineService.returnMoney(id);
        return ResponseEntity.ok(ApiResponse.ok("Funds returned to savings fund successfully", record));
    }
}
