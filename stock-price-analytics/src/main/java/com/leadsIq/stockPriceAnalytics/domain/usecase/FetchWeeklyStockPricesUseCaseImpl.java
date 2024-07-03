package com.leadsIq.stockPriceAnalytics.domain.usecase;

import org.springframework.stereotype.Service;

import com.leadsIq.stockPriceAnalytics.domain.entity.FetchStockPricesFilter;
import com.leadsIq.stockPriceAnalytics.domain.entity.StockPrice;
import com.leadsIq.stockPriceAnalytics.domain.gateway.StockPriceGateway;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FetchWeeklyStockPricesUseCaseImpl
    implements FetchWeeklyStockPricesUseCase {

    private final StockPriceGateway stockPriceGateway;

    @Override
    public List<StockPrice> execute(FetchStockPricesFilter filter) {
        List<StockPrice> stockPrices =
            stockPriceGateway.findByDateRange(filter.startDate(), filter.endDate());

        Map<String, List<StockPrice>> groupedByCompany = stockPrices.stream()
            .collect(Collectors.groupingBy(StockPrice::company));

        return groupedByCompany
            .entrySet()
            .stream()
            .map(entry -> calculateWeeklyAverage(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    private StockPrice calculateWeeklyAverage(String company, List<StockPrice> stockPrices) {
        return new StockPrice(
            company,
            null,
            stockPrices.stream().mapToDouble(StockPrice::open).average().orElse(0.0),
            stockPrices.stream().mapToDouble(StockPrice::close).average().orElse(0.0),
            stockPrices.stream().mapToDouble(StockPrice::high).average().orElse(0.0),
            stockPrices.stream().mapToDouble(StockPrice::low).average().orElse(0.0),
            (long) stockPrices.stream().mapToLong(StockPrice::volume).average().orElse(0L)
        );
    }
}
