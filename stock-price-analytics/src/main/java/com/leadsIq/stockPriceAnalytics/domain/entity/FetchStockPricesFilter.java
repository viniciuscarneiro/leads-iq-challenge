package com.leadsIq.stockPriceAnalytics.domain.entity;

import java.time.LocalDate;


public record FetchStockPricesFilter(LocalDate startDate, LocalDate endDate) {
}
