package com.leadsIq.stockPriceAnalytics.domain.usecase;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.leadsIq.stockPriceAnalytics.domain.entity.FetchStockPriceFilter;
import com.leadsIq.stockPriceAnalytics.domain.entity.StockPrice;
import com.leadsIq.stockPriceAnalytics.domain.gateway.StockPriceGateway;
import java.time.LocalDate;

public class FetchStockPriceFilteringBySymbolAndDateUseCaseTest {

    @Test
    public void testExecute() {
        StockPriceGateway stockPriceGateway = Mockito.mock(StockPriceGateway.class);
        FetchStockPriceFilteringBySymbolAndDateUseCaseImpl useCase =
            new FetchStockPriceFilteringBySymbolAndDateUseCaseImpl(stockPriceGateway);

        LocalDate date = LocalDate.of(2024, 7, 3);
        String symbol = "AAPL";
        FetchStockPriceFilter filter = new FetchStockPriceFilter(date, symbol);
        StockPrice expectedStockPrice =
            new StockPrice(symbol, date, 150.0, 155.0, 157.0, 149.0, 1000000L);

        Mockito.when(stockPriceGateway.findByCompanySymbolAndDate(symbol, date))
            .thenReturn(expectedStockPrice);

        StockPrice result = useCase.execute(filter);

        assertEquals(expectedStockPrice, result);
        Mockito.verify(stockPriceGateway, Mockito.times(1))
            .findByCompanySymbolAndDate(symbol, date);
    }
}
