package com.businesstracker.backend.controller;

import com.businesstracker.backend.Userdetail;
import com.businesstracker.backend.dto.ApiResponse;
import com.businesstracker.backend.dto.BalanceUpdateRequest;
import com.businesstracker.backend.service.UserDetailService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserDetailController {

    private final UserDetailService userDetailService;

    public UserDetailController(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Userdetail>> getUserDetails() {
        Userdetail user = userDetailService.getOrCreateUser();
        return ResponseEntity.ok(ApiResponse.ok("User details retrieved", user));
    }

    @PutMapping("/balance")
    public ResponseEntity<ApiResponse<Userdetail>> updateBalances(
            @Valid @RequestBody BalanceUpdateRequest request) {
        Userdetail updated = userDetailService.updateBalances(
                request.getBusinessBalance(),
                request.getSavingsBalance()
        );
        return ResponseEntity.ok(ApiResponse.ok("User balances updated successfully", updated));
    }
}
