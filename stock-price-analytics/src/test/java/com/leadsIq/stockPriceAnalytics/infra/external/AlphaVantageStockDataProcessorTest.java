package com.leadsIq.stockPriceAnalytics.infra.external;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.leadsIq.stockPriceAnalytics.domain.entity.StockPrice;
import com.leadsIq.stockPriceAnalytics.infra.adapter.StockDataProcessor;
import com.leadsIq.stockPriceAnalytics.infra.adapter.external.alphavantage.AlphaVantageStockDataProcessorImpl;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AlphaVantageStockDataProcessorTest {

    @Mock
    private StockDataProcessor stockDataProcessor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessStockData() {
        Map<String, Map<String, Object>> mockStockData = createMockStockData();
        LocalDate startDate = LocalDate.of(2024, 1, 1);

        when(stockDataProcessor.processStockData(eq(startDate), any()))
            .thenReturn(processStockData(startDate, mockStockData));

        Set<StockPrice> result = stockDataProcessor.processStockData(startDate, mockStockData);

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    private Map<String, Map<String, Object>> createMockStockData() {
        Map<String, Map<String, Object>> mockData = new HashMap<>();

        Map<String, Object> metaData = new HashMap<>();
        metaData.put("2. Symbol", "AAPL"); // Example symbol
        mockData.put("Meta Data", metaData);

        Map<String, Object> timeSeries = new HashMap<>();
        LocalDate date = LocalDate.of(2024, 1, 1);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        timeSeries.put(formatter.format(date), createMockDailyResult());
        date = date.plusDays(1);
        timeSeries.put(formatter.format(date), createMockDailyResult());
        date = date.plusDays(1);
        timeSeries.put(formatter.format(date), createMockDailyResult());
        mockData.put("Time Series (Daily)", timeSeries);

        return mockData;
    }

    private Map<String, Object> createMockDailyResult() {
        Map<String, Object> dailyResult = new HashMap<>();
        dailyResult.put("1. open", 100.0);
        dailyResult.put("2. high", 105.0);
        dailyResult.put("3. low", 95.0);
        dailyResult.put("4. close", 102.5);
        dailyResult.put("5. volume", 1000000L);
        return dailyResult;
    }

    private Set<StockPrice> processStockData(LocalDate startDate,
                                             Map<String, Map<String, Object>> stockData) {
        AlphaVantageStockDataProcessorImpl processor = new AlphaVantageStockDataProcessorImpl();
        return processor.processStockData(startDate, stockData);
    }
}
