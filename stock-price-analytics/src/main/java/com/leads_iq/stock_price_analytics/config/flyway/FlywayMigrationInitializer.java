package com.leads_iq.stock_price_analytics.config.flyway;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FlywayMigrationInitializer
    extends org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer {

    public FlywayMigrationInitializer(Flyway flyway,
                                      FlywayMigrationStrategy migrationStrategy) {
        super(flyway, migrationStrategy);
    }

    public void afterPropertiesSet() throws Exception {
        log.warn("Not running Flyway migrations at application startup");
    }
}
