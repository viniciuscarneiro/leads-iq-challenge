package com.leadsIq.stockPriceAnalytics.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    @Value("${alphavantage.api.key}")
    private String apiKey;

    @Bean
    public String apiKey() {
        return this.apiKey;
    }
}
