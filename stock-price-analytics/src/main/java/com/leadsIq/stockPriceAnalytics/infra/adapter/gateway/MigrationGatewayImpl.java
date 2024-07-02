package com.leadsIq.stockPriceAnalytics.infra.adapter.gateway;


import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.springframework.stereotype.Service;

import com.leadsIq.stockPriceAnalytics.domain.gateway.MigrationGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MigrationGatewayImpl implements MigrationGateway {

    private final Flyway flyway;

    public void initializeDatabase() {
        log.info("Initializing database...");
        try {
            flyway.migrate(); // runs the migrations
        } catch (FlywayException e) {
            retry(e);
        }
        log.info("Finish database initialization.");
    }

    private void retry(FlywayException e) {
        if (e.getMessage().contains("no schema history table")) { // need to run baseline first to set up the table
            flyway.baseline(); // then try to run the migrations again
            flyway.migrate();
        } else {
            throw e;
        }
    }
}
