package com.leadsIq.stockPriceAnalytics.domain.usecase;

import com.leadsIq.stockPriceAnalytics.domain.entity.FetchStockPricesFilter;
import com.leadsIq.stockPriceAnalytics.domain.entity.StockPrice;
import java.util.List;

public interface FetchWeeklyStockPricesUseCase {
    List<StockPrice> execute(FetchStockPricesFilter filter);
}
