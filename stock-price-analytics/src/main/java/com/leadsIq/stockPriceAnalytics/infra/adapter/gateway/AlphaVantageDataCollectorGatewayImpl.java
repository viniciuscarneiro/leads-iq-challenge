package com.leadsIq.stockPriceAnalytics.infra.adapter.gateway;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.leadsIq.stockPriceAnalytics.domain.entity.CollectDataFilter;
import com.leadsIq.stockPriceAnalytics.domain.entity.StockPrice;
import com.leadsIq.stockPriceAnalytics.domain.gateway.DataCollectorGateway;
import com.leadsIq.stockPriceAnalytics.infra.adapter.StockDataProcessor;
import com.leadsIq.stockPriceAnalytics.infra.adapter.external.alphavantage.AlphaVantageApi;
import com.leadsIq.stockPriceAnalytics.infra.repository.CompanyRepository;
import com.leadsIq.stockPriceAnalytics.infra.repository.StockPriceRepository;
import com.leadsIq.stockPriceAnalytics.infra.repository.entity.CompanyEntity;
import com.leadsIq.stockPriceAnalytics.infra.repository.entity.StockPriceEntity;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlphaVantageDataCollectorGatewayImpl implements DataCollectorGateway {

    private final AlphaVantageApi alphaVantageApi;
    private final StockDataProcessor stockDataProcessor;
    private final CompanyRepository companyRepository;
    private final StockPriceRepository stockPriceRepository;
    private final String apiKey;

    @Override
    public void collectStockData(CollectDataFilter collectDataFilter) {
        for (String symbol : collectDataFilter.symbols()) {
            Map<String, Map<String, Object>> apiResponse =
                alphaVantageApi.getStockData("TIME_SERIES_DAILY", symbol, apiKey, "full");
            Set<StockPrice> stockPrices =
                stockDataProcessor.processStockData(collectDataFilter.startDate(), apiResponse);
            CompanyEntity companyEntity = companyRepository.findBySymbol(symbol).orElseGet(() -> {
                CompanyEntity newCompanyEntity = new CompanyEntity();
                newCompanyEntity.setSymbol(symbol);
                newCompanyEntity.setCreatedAt(LocalDateTime.now());
                return companyRepository.save(newCompanyEntity);
            });
            for (StockPrice stockPrice : stockPrices) {
                try {
                    stockPriceRepository.save(StockPriceEntity.of(stockPrice, companyEntity));
                } catch (DataIntegrityViolationException e) {
                    if (!e.getMessage().contains("stock_price.unique_company_id_date")) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
