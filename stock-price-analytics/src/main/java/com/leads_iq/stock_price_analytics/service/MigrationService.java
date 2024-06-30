package com.leads_iq.stock_price_analytics.service;


import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.output.MigrateResult;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MigrationService {

    private final Flyway flyway;

    public void initializeDatabase() {
        log.info("Initializing database...");
        MigrateResult result = null;
        try {
            result = flyway.migrate(); // runs the migrations
        } catch (FlywayException e) {
            if (e.getMessage().contains(
                "no schema history table")) { // need to run baseline first to set up the table
                flyway.baseline(); // then try to run the migrations again
                result = flyway.migrate();
            } else {
                log.error(e.getMessage(), e);
            }
        }
    }
}
