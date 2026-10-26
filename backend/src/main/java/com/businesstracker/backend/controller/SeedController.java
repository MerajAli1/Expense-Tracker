package com.businesstracker.backend.controller;

import com.businesstracker.backend.config.DataInitializer;
import com.businesstracker.backend.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seed")
@CrossOrigin(origins = "*")
public class SeedController {

    private final DataInitializer dataInitializer;

    public SeedController(DataInitializer dataInitializer) {
        this.dataInitializer = dataInitializer;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> reseedData() {
        dataInitializer.seedAll();
        return ResponseEntity.ok(ApiResponse.ok("Demo data seeded successfully into MongoDB.", null));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> resetData() {
        dataInitializer.clearAll();
        dataInitializer.seedAll();
        return ResponseEntity.ok(ApiResponse.ok("Database reset and reseeded with clean demo data.", null));
    }
}
