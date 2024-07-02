package com.leadsIq.stockPriceAnalytics.domain.usecase;

import org.springframework.stereotype.Service;

import com.leadsIq.stockPriceAnalytics.domain.entity.FetchStockPriceFilter;
import com.leadsIq.stockPriceAnalytics.domain.entity.StockPrice;
import com.leadsIq.stockPriceAnalytics.domain.gateway.StockPriceGateway;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FetchStockPriceFilteringBySymbolAndDateUseCaseImpl
    implements FetchStockPriceFilteringBySymbolAndDateUseCase {
    private final StockPriceGateway stockPriceGateway;

    @Override
    public StockPrice execute(FetchStockPriceFilter filter) {
        return stockPriceGateway.findByCompanySymbolAndDate(filter.symbol(), filter.date());
    }
}
