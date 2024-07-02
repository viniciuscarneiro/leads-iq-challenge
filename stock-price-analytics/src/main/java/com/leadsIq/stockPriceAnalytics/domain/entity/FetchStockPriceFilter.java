package com.leadsIq.stockPriceAnalytics.domain.entity;

import java.time.LocalDate;


public record FetchStockPriceFilter(LocalDate date,
                                    String symbol) {
}
