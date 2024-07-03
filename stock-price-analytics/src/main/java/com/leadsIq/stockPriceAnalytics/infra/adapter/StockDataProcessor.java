package com.leadsIq.stockPriceAnalytics.infra.adapter;

import com.leadsIq.stockPriceAnalytics.domain.entity.StockPrice;
import java.time.LocalDate;
import java.util.Set;

public interface StockDataProcessor {
    Set<StockPrice> processStockData(LocalDate startDate, Object data);
}
