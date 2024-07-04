package com.leadsIq.stockPriceAnalytics.infra.config.flyway;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Bean
    public org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer flywayInitializer(
        Flyway flyway,
        ObjectProvider<FlywayMigrationStrategy> migrationStrategy) {
        return new FlywayMigrationInitializer(flyway, migrationStrategy.getIfAvailable());
    }
}