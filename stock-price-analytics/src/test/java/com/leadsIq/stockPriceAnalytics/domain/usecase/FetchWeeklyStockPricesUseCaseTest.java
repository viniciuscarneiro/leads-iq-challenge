package com.leadsIq.stockPriceAnalytics.domain.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.leadsIq.stockPriceAnalytics.domain.entity.FetchStockPricesFilter;
import com.leadsIq.stockPriceAnalytics.domain.entity.StockPrice;
import com.leadsIq.stockPriceAnalytics.domain.gateway.StockPriceGateway;
import java.time.LocalDate;
import java.util.List;

public class FetchWeeklyStockPricesUseCaseTest {

    @Test
    public void testExecute() {
        StockPriceGateway stockPriceGateway = Mockito.mock(StockPriceGateway.class);
        FetchWeeklyStockPricesUseCaseImpl useCase =
            new FetchWeeklyStockPricesUseCaseImpl(stockPriceGateway);

        LocalDate startDate = LocalDate.of(2024, 7, 1);
        LocalDate endDate = LocalDate.of(2024, 7, 7);
        FetchStockPricesFilter filter = new FetchStockPricesFilter(startDate, endDate);

        List<StockPrice> stockPrices = List.of(
            new StockPrice("AAPL", startDate, 150.0, 155.0, 157.0, 149.0, 1000000L),
            new StockPrice("AAPL", endDate, 152.0, 158.0, 160.0, 150.0, 1200000L),
            new StockPrice("GOOGL", startDate, 2700.0, 2750.0, 2770.0, 2690.0, 800000L),
            new StockPrice("GOOGL", endDate, 2720.0, 2780.0, 2800.0, 2700.0, 900000L)
        );

        Mockito.when(stockPriceGateway.findByDateRange(startDate, endDate)).thenReturn(stockPrices);

        List<StockPrice> result = useCase.execute(filter);

        StockPrice expectedAaplAverage =
            new StockPrice("AAPL", null, 151.0, 156.5, 158.5, 149.5, 1100000L);
        StockPrice expectedGooglAverage =
            new StockPrice("GOOGL", null, 2710.0, 2765.0, 2785.0, 2695.0, 850000L);

        assertEquals(2, result.size());
        assertEquals(List.of(expectedGooglAverage, expectedAaplAverage), result);
        Mockito.verify(stockPriceGateway, Mockito.times(1)).findByDateRange(startDate, endDate);
    }
}
