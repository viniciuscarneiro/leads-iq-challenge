package com.leadsIq.stockPriceAnalytics.infra.adapter.external.alphavantage;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AlphaVantageApiStaticFallback implements AlphaVantageApi {
    @Override
    public Map<String, Map<String, Object>> getStockData(String function, String symbol,
                                                         String apikey,
                                                         String outputsize) {
        return Cache.getData(symbol);
    }
}
