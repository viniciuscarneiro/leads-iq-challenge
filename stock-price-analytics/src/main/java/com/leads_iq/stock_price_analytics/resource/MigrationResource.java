package com.leads_iq.stock_price_analytics.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leads_iq.stock_price_analytics.service.MigrationService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class MigrationResource {

    private final MigrationService migrationService;

    @PostMapping("/db_create")
    public ResponseEntity<?> createDatabase() {
        migrationService.initializeDatabase();
        return ResponseEntity.ok().build();
    }
}