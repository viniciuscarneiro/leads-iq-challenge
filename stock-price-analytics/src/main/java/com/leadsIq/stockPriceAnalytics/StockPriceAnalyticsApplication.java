package com.leadsIq.stockPriceAnalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class StockPriceAnalyticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockPriceAnalyticsApplication.class, args);
	}
}
