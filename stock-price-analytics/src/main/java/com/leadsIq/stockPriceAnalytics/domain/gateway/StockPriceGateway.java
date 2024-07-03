package com.leadsIq.stockPriceAnalytics.domain.gateway;

import com.leadsIq.stockPriceAnalytics.domain.entity.StockPrice;
import java.time.LocalDate;
import java.util.List;

public interface StockPriceGateway {
    StockPrice findByCompanySymbolAndDate(String symbol, LocalDate date);

    List<StockPrice> findByDateRange(LocalDate startDate, LocalDate endDate);
}
