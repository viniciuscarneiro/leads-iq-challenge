package com.leadsIq.stockPriceAnalytics.infra.gateway;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import com.leadsIq.stockPriceAnalytics.domain.entity.CollectDataFilter;
import com.leadsIq.stockPriceAnalytics.domain.entity.StockPrice;
import com.leadsIq.stockPriceAnalytics.infra.adapter.StockDataProcessor;
import com.leadsIq.stockPriceAnalytics.infra.adapter.external.alphavantage.AlphaVantageApi;
import com.leadsIq.stockPriceAnalytics.infra.adapter.gateway.AlphaVantageDataCollectorGatewayImpl;
import com.leadsIq.stockPriceAnalytics.infra.repository.CompanyRepository;
import com.leadsIq.stockPriceAnalytics.infra.repository.StockPriceRepository;
import com.leadsIq.stockPriceAnalytics.infra.repository.entity.CompanyEntity;
import com.leadsIq.stockPriceAnalytics.infra.repository.entity.StockPriceEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

class AlphaVantageDataCollectorGatewayTest {

    @Mock
    private AlphaVantageApi alphaVantageApi;

    @Mock
    private StockDataProcessor stockDataProcessor;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private StockPriceRepository stockPriceRepository;

    private AlphaVantageDataCollectorGatewayImpl alphaVantageDataCollectorGateway;

    private final String apiKey = "test";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        alphaVantageDataCollectorGateway =
            new AlphaVantageDataCollectorGatewayImpl(alphaVantageApi, stockDataProcessor,
                companyRepository, stockPriceRepository, apiKey);
    }

    @Test
    void testCollectStockData_Success() {
        String symbol = "AAPL";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        Map<String, Map<String, Object>> apiResponse = new HashMap<>();
        Set<StockPrice> stockPrices = new HashSet<>();
        stockPrices.add(new StockPrice(symbol, startDate, 100.0, 110.0, 120.0, 90.0, 1000L));

        when(alphaVantageApi.getStockData(eq("TIME_SERIES_DAILY"), eq(symbol), eq(apiKey),
            eq("full")))
            .thenReturn(apiResponse);
        when(stockDataProcessor.processStockData(eq(startDate), any()))
            .thenReturn(stockPrices);

        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setId(1L);
        companyEntity.setSymbol(symbol);
        companyEntity.setCreatedAt(LocalDateTime.now());

        when(companyRepository.findBySymbol(symbol)).thenReturn(Optional.empty());
        when(companyRepository.save(any(CompanyEntity.class))).thenReturn(companyEntity);

        CollectDataFilter filter = new CollectDataFilter(startDate, Set.of(symbol));
        alphaVantageDataCollectorGateway.collectStockData(filter);

        verify(alphaVantageApi, times(1)).getStockData(eq("TIME_SERIES_DAILY"), eq(symbol),
            anyString(), eq("full"));
        verify(stockDataProcessor, times(1)).processStockData(eq(startDate), any());
        verify(companyRepository, times(1)).findBySymbol(symbol);
        verify(companyRepository, times(1)).save(any(CompanyEntity.class));
        verify(stockPriceRepository, times(1)).save(any(StockPriceEntity.class));
    }

    @Test
    void testCollectStockData_WithExistingCompany() {
        String symbol = "AAPL";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        Map<String, Map<String, Object>> apiResponse = new HashMap<>();
        Set<StockPrice> stockPrices = new HashSet<>();
        stockPrices.add(new StockPrice(symbol, startDate, 100.0, 110.0, 120.0, 90.0, 1000L));

        when(alphaVantageApi.getStockData(eq("TIME_SERIES_DAILY"), eq(symbol), anyString(),
            eq("full")))
            .thenReturn(apiResponse);
        when(stockDataProcessor.processStockData(eq(startDate), any()))
            .thenReturn(stockPrices);

        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setId(1L);
        companyEntity.setSymbol(symbol);
        companyEntity.setCreatedAt(LocalDateTime.now());

        when(companyRepository.findBySymbol(symbol)).thenReturn(Optional.of(companyEntity));

        CollectDataFilter filter = new CollectDataFilter(startDate, Set.of(symbol));
        alphaVantageDataCollectorGateway.collectStockData(filter);

        verify(alphaVantageApi, times(1)).getStockData(eq("TIME_SERIES_DAILY"), eq(symbol),
            eq(apiKey), eq("full"));
        verify(stockDataProcessor, times(1)).processStockData(eq(startDate), any());
        verify(companyRepository, times(1)).findBySymbol(symbol);
        verify(companyRepository, never()).save(any(CompanyEntity.class));
        verify(stockPriceRepository, times(1)).save(any(StockPriceEntity.class));
    }

    @Test
    void testCollectStockData_WithUniqueConstraintViolation() {
        String symbol = "AAPL";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        Map<String, Map<String, Object>> apiResponse = new HashMap<>();
        Set<StockPrice> stockPrices = new HashSet<>();
        stockPrices.add(new StockPrice(symbol, startDate, 100.0, 110.0, 120.0, 90.0, 1000L));

        when(alphaVantageApi.getStockData(eq("TIME_SERIES_DAILY"), eq(symbol), anyString(),
            eq("full")))
            .thenReturn(apiResponse);
        when(stockDataProcessor.processStockData(eq(startDate), any()))
            .thenReturn(stockPrices);

        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setId(1L);
        companyEntity.setSymbol(symbol);
        companyEntity.setCreatedAt(LocalDateTime.now());

        when(companyRepository.findBySymbol(symbol)).thenReturn(Optional.of(companyEntity));
        doThrow(new DataIntegrityViolationException("stock_price.unique_company_id_date"))
            .when(stockPriceRepository).save(any(StockPriceEntity.class));

        CollectDataFilter filter = new CollectDataFilter(startDate, Set.of(symbol));
        alphaVantageDataCollectorGateway.collectStockData(filter);

        verify(alphaVantageApi, times(1)).getStockData(eq("TIME_SERIES_DAILY"), eq(symbol),
            eq(apiKey), eq("full"));
        verify(stockDataProcessor, times(1)).processStockData(eq(startDate), any());
        verify(companyRepository, times(1)).findBySymbol(symbol);
        verify(companyRepository, never()).save(any(CompanyEntity.class));
        verify(stockPriceRepository, times(1)).save(any(StockPriceEntity.class));
    }

    @Test
    void testCollectStockData_WithOtherDataIntegrityViolation() {
        String symbol = "AAPL";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        Map<String, Map<String, Object>> apiResponse = new HashMap<>();
        Set<StockPrice> stockPrices = new HashSet<>();
        stockPrices.add(new StockPrice(symbol, startDate, 100.0, 110.0, 120.0, 90.0, 1000L));

        when(alphaVantageApi.getStockData(eq("TIME_SERIES_DAILY"), eq(symbol), anyString(),
            eq("full")))
            .thenReturn(apiResponse);
        when(stockDataProcessor.processStockData(eq(startDate), any()))
            .thenReturn(stockPrices);

        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setId(1L);
        companyEntity.setSymbol(symbol);
        companyEntity.setCreatedAt(LocalDateTime.now());

        when(companyRepository.findBySymbol(symbol)).thenReturn(Optional.of(companyEntity));
        doThrow(new DataIntegrityViolationException("Some other error"))
            .when(stockPriceRepository).save(any(StockPriceEntity.class));

        CollectDataFilter filter = new CollectDataFilter(startDate, Set.of(symbol));

        assertThrows(RuntimeException.class,
            () -> alphaVantageDataCollectorGateway.collectStockData(filter));

        verify(alphaVantageApi, times(1)).getStockData(eq("TIME_SERIES_DAILY"), eq(symbol),
            eq(apiKey), eq("full"));
        verify(stockDataProcessor, times(1)).processStockData(eq(startDate), any());
        verify(companyRepository, times(1)).findBySymbol(symbol);
        verify(companyRepository, never()).save(any(CompanyEntity.class));
        verify(stockPriceRepository, times(1)).save(any(StockPriceEntity.class));
    }
}
