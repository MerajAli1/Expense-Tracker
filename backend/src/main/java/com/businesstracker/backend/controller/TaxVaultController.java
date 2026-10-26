package com.businesstracker.backend.controller;

import com.businesstracker.backend.TaxLiabilityVault;
import com.businesstracker.backend.dto.ApiResponse;
import com.businesstracker.backend.dto.ReceiptProcessRequest;
import com.businesstracker.backend.dto.ReceiptProcessResponse;
import com.businesstracker.backend.service.TaxVaultService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vault")
@CrossOrigin(origins = "*")
public class TaxVaultController {

    private final TaxVaultService taxVaultService;

    public TaxVaultController(TaxVaultService taxVaultService) {
        this.taxVaultService = taxVaultService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<TaxLiabilityVault>> getVaultState() {
        TaxLiabilityVault vault = taxVaultService.getOrCreateVault();
        return ResponseEntity.ok(ApiResponse.ok("Vault state retrieved", vault));
    }

    @PostMapping("/process-receipt")
    public ResponseEntity<ApiResponse<ReceiptProcessResponse>> processReceipt(
            @Valid @RequestBody ReceiptProcessRequest request) {
        ReceiptProcessResponse response = taxVaultService.processReceipt(request);
        return ResponseEntity.ok(ApiResponse.ok("Receipt processed and tax isolated successfully", response));
    }

    @PutMapping("/emergency-lock")
    public ResponseEntity<ApiResponse<TaxLiabilityVault>> setEmergencyLock(
            @RequestParam boolean lock) {
        TaxLiabilityVault vault = taxVaultService.toggleEmergencyLock(lock);
        String msg = lock ? "Emergency fund locked" : "Emergency fund unlocked";
        return ResponseEntity.ok(ApiResponse.ok(msg, vault));
    }

    @PutMapping("/cash")
    public ResponseEntity<ApiResponse<TaxLiabilityVault>> updateCash(
            @RequestParam double amount) {
        TaxLiabilityVault vault = taxVaultService.updateUsableCash(amount);
        return ResponseEntity.ok(ApiResponse.ok("Usable cash updated successfully", vault));
    }
}
