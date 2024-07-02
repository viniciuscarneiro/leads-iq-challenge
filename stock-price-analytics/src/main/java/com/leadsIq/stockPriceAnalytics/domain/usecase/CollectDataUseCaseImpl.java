package com.leadsIq.stockPriceAnalytics.domain.usecase;

import org.springframework.stereotype.Service;

import com.leadsIq.stockPriceAnalytics.domain.entity.CollectDataFilter;
import com.leadsIq.stockPriceAnalytics.domain.gateway.DataCollectorGateway;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CollectDataUseCaseImpl implements CollectDataUseCase {
    private final DataCollectorGateway dataCollectorGateway;

    @Override
    public void execute(CollectDataFilter collectDataFilter) {
        dataCollectorGateway.collectStockData(collectDataFilter);
    }
}
