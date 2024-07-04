package com.leadsIq.stockPriceAnalytics.infra.gateway;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.leadsIq.stockPriceAnalytics.infra.adapter.gateway.MigrationGatewayImpl;

class MigrationGatewayTest {

    private Flyway flyway;
    private MigrationGatewayImpl migrationGateway;

    @BeforeEach
    void setUp() {
        flyway = Mockito.mock(Flyway.class);
        migrationGateway = new MigrationGatewayImpl(flyway);
    }

    @Test
    void testInitializeDatabase_Success() {
        migrationGateway.initializeDatabase();
        verify(flyway, times(1)).migrate();
        verify(flyway, never()).baseline();
    }

    @Test
    void testInitializeDatabase_WithRetry_Success() {
        doThrow(new FlywayException("no schema history table")).doAnswer(invocation -> null).when(flyway).migrate();

        migrationGateway.initializeDatabase();

        verify(flyway, times(2)).migrate();
        verify(flyway, times(1)).baseline();
    }

    @Test
    void testInitializeDatabase_WithRetry_Failure() {
        doThrow(new FlywayException("Some other error")).when(flyway).migrate();

        FlywayException exception =
            assertThrows(FlywayException.class, () -> migrationGateway.initializeDatabase());

        assertEquals("Some other error", exception.getMessage());
        verify(flyway, times(1)).migrate();
        verify(flyway, never()).baseline();
    }
}
