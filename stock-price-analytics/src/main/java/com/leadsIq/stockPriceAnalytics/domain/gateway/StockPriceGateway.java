package com.leadsIq.stockPriceAnalytics.domain.gateway;

import com.leadsIq.stockPriceAnalytics.domain.entity.StockPrice;
import java.time.LocalDate;

public interface StockPriceGateway {
    StockPrice findByCompanySymbolAndDate(String symbol, LocalDate date);
}
