package com.leadsIq.stockPriceAnalytics.domain.usecase;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.leadsIq.stockPriceAnalytics.domain.gateway.MigrationGateway;

public class CreateDatabaseAndSchemasUseCaseTest {

    @Test
    public void testExecute() {
        MigrationGateway migrationGateway = Mockito.mock(MigrationGateway.class);
        CreateDatabaseAndSchemasUseCaseImpl useCase =
            new CreateDatabaseAndSchemasUseCaseImpl(migrationGateway);

        useCase.execute();

        Mockito.verify(migrationGateway, Mockito.times(1)).initializeDatabase();
    }
}
