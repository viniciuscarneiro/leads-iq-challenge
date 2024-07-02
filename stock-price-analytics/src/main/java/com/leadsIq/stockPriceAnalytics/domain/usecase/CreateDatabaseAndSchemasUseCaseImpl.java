package com.leadsIq.stockPriceAnalytics.domain.usecase;

import org.springframework.stereotype.Service;

import com.leadsIq.stockPriceAnalytics.domain.gateway.MigrationGateway;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateDatabaseAndSchemasUseCaseImpl implements CreateDatabaseAndSchemasUseCase {
    private final MigrationGateway migrationGateway;

    @Override
    public void execute() {
        migrationGateway.initializeDatabase();
    }
}
