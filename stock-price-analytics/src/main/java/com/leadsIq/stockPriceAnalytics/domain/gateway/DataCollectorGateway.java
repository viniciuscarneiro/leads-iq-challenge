package com.leadsIq.stockPriceAnalytics.domain.gateway;

import com.leadsIq.stockPriceAnalytics.domain.entity.CollectDataFilter;

public interface DataCollectorGateway {
    void collectStockData(CollectDataFilter collectDataFilter);
}
