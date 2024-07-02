package com.leadsIq.stockPriceAnalytics.infra.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leadsIq.stockPriceAnalytics.domain.usecase.CreateDatabaseAndSchemasUseCase;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class MigrationResource {

    private final CreateDatabaseAndSchemasUseCase createDatabaseAndSchemasUseCase;

    @PostMapping("/db_create")
    public ResponseEntity<?> createDatabase() {
        createDatabaseAndSchemasUseCase.execute();
        return ResponseEntity.ok().build();
    }
}