package com.leadsIq.stockPriceAnalytics.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public record StockPrice(String company, LocalDate date, BigDecimal open, BigDecimal close,
                         BigDecimal high, BigDecimal low, Long volume) {

}
