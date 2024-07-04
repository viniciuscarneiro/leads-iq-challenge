package com.leadsIq.stockPriceAnalytics.domain.entity;

import java.time.LocalDate;

public record StockPrice(String company, LocalDate date, Double open, Double close, Double high,
                         Double low, Long volume) {

}
