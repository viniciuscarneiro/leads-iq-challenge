package com.leadsIq.stockPriceAnalytics.infra.gateway;

import static com.leadsIq.stockPriceAnalytics.domain.util.Constants.STOCK_PRICE_NOT_FOUND_ERROR_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.leadsIq.stockPriceAnalytics.domain.entity.StockPrice;
import com.leadsIq.stockPriceAnalytics.domain.exception.EntityNotFoundException;
import com.leadsIq.stockPriceAnalytics.domain.gateway.StockPriceGateway;
import com.leadsIq.stockPriceAnalytics.infra.adapter.gateway.StockPriceGatewayImpl;
import com.leadsIq.stockPriceAnalytics.infra.repository.StockPriceRepository;
import com.leadsIq.stockPriceAnalytics.infra.repository.entity.CompanyEntity;
import com.leadsIq.stockPriceAnalytics.infra.repository.entity.StockPriceEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class StockPriceGatewayImplTest {

    private StockPriceRepository stockPriceRepository;
    private StockPriceGateway stockPriceGateway;

    @BeforeEach
    public void setup() {
        stockPriceRepository = Mockito.mock(StockPriceRepository.class);
        stockPriceGateway = new StockPriceGatewayImpl(stockPriceRepository);
    }

    @Test
    public void testFindByCompanySymbolAndDate_Success() {
        String symbol = "AAPL";
        LocalDate date = LocalDate.of(2024, 7, 3);
        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setId(1L);
        companyEntity.setSymbol(symbol);
        companyEntity.setCreatedAt(LocalDate.now().atStartOfDay());
        StockPriceEntity stockPriceEntity = new StockPriceEntity();
        stockPriceEntity.setCompanyEntity(companyEntity);
        stockPriceEntity.setDate(date);
        stockPriceEntity.setOpen(150.0);
        stockPriceEntity.setClose(155.0);
        stockPriceEntity.setHigh(157.0);
        stockPriceEntity.setLow(149.0);
        stockPriceEntity.setVolume(1000000L);
        stockPriceEntity.setCreatedAt(LocalDate.now().atStartOfDay());
        when(stockPriceRepository.findByCompanyEntitySymbolAndDate(symbol, date)).thenReturn(
            Optional.of(stockPriceEntity));

        StockPrice result = stockPriceGateway.findByCompanySymbolAndDate(symbol, date);

        assertEquals(stockPriceEntity.toDomain(), result);
    }

    @Test
    public void testFindByCompanySymbolAndDate_NotFound() {
        String symbol = "AAPL";
        LocalDate date = LocalDate.of(2024, 7, 3);
        when(stockPriceRepository.findByCompanyEntitySymbolAndDate(symbol, date)).thenReturn(
            Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            stockPriceGateway.findByCompanySymbolAndDate(symbol, date);
        });

        assertEquals(String.format(STOCK_PRICE_NOT_FOUND_ERROR_MESSAGE, symbol, date),
            exception.getMessage());
    }

    @Test
    public void testFindByDateRange() {
        LocalDate startDate = LocalDate.of(2024, 7, 1);
        LocalDate endDate = LocalDate.of(2024, 7, 7);
        CompanyEntity companyEntityAAPL = new CompanyEntity();
        companyEntityAAPL.setId(1L);
        companyEntityAAPL.setSymbol("AAPL");
        companyEntityAAPL.setCreatedAt(LocalDate.now().atStartOfDay());

        CompanyEntity companyEntityGOOGL = new CompanyEntity();
        companyEntityGOOGL.setId(2L);
        companyEntityGOOGL.setSymbol("GOOGL");
        companyEntityGOOGL.setCreatedAt(LocalDate.now().atStartOfDay());

        List<StockPriceEntity> stockPriceEntities = List.of(
            createStockPriceEntity(companyEntityAAPL, startDate, 150.0, 155.0, 157.0, 149.0,
                1000000L),
            createStockPriceEntity(companyEntityAAPL, endDate, 152.0, 158.0, 160.0, 150.0,
                1200000L),
            createStockPriceEntity(companyEntityGOOGL, startDate, 2700.0, 2750.0, 2770.0, 2690.0,
                800000L),
            createStockPriceEntity(companyEntityGOOGL, endDate, 2720.0, 2780.0, 2800.0, 2700.0,
                900000L)
        );
        when(stockPriceRepository.findByDateBetween(startDate, endDate)).thenReturn(
            stockPriceEntities);

        List<StockPrice> result = stockPriceGateway.findByDateRange(startDate, endDate);

        List<StockPrice> expectedStockPrices = stockPriceEntities.stream()
            .map(StockPriceEntity::toDomain)
            .toList();

        assertEquals(expectedStockPrices, result);
    }

    private StockPriceEntity createStockPriceEntity(CompanyEntity companyEntity, LocalDate date,
                                                    Double open, Double close, Double high,
                                                    Double low, Long volume) {
        StockPriceEntity stockPriceEntity = new StockPriceEntity();
        stockPriceEntity.setCompanyEntity(companyEntity);
        stockPriceEntity.setDate(date);
        stockPriceEntity.setOpen(open);
        stockPriceEntity.setClose(close);
        stockPriceEntity.setHigh(high);
        stockPriceEntity.setLow(low);
        stockPriceEntity.setVolume(volume);
        stockPriceEntity.setCreatedAt(LocalDate.now().atStartOfDay());
        return stockPriceEntity;
    }
}
