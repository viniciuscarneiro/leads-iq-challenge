package com.leadsIq.stockPriceAnalytics.domain.usecase;

import com.leadsIq.stockPriceAnalytics.domain.entity.FetchStockPriceFilter;
import com.leadsIq.stockPriceAnalytics.domain.entity.StockPrice;

public interface FetchStockPriceFilteringBySymbolAndDateUseCase {
    StockPrice execute(FetchStockPriceFilter filter);
}
