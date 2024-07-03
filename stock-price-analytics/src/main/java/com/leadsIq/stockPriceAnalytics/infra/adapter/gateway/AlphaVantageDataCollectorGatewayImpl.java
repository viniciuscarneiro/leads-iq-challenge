package com.leadsIq.stockPriceAnalytics.infra.adapter.gateway;

import org.springframework.stereotype.Service;

import com.leadsIq.stockPriceAnalytics.domain.entity.CollectDataFilter;
import com.leadsIq.stockPriceAnalytics.domain.entity.StockPrice;
import com.leadsIq.stockPriceAnalytics.domain.gateway.DataCollectorGateway;
import com.leadsIq.stockPriceAnalytics.infra.adapter.StockDataProcessor;
import com.leadsIq.stockPriceAnalytics.infra.adapter.external.AlphaVantageApi;
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

    private final AlphaVantageApi alphaVantageService;
    private final StockDataProcessor stockDataProcessor;
    private final CompanyRepository companyRepository;
    private final StockPriceRepository stockPriceRepository;

    @Override
    public void collectStockData(CollectDataFilter collectDataFilter) {
        for (String symbol : collectDataFilter.symbols()) {
            Map<String, Map<String, Object>> apiResponse = alphaVantageService.getStockData(symbol);
            Set<StockPrice> stockPrices = stockDataProcessor.processStockData(collectDataFilter.startDate(), apiResponse);
            CompanyEntity companyEntity = companyRepository.findBySymbol(symbol).orElseGet(() -> {
                CompanyEntity newCompanyEntity = new CompanyEntity();
                newCompanyEntity.setSymbol(symbol);
                newCompanyEntity.setCreatedAt(LocalDateTime.now());
                return companyRepository.save(newCompanyEntity);
            });

            for (StockPrice stockPrice : stockPrices) {
                stockPriceRepository.save(
                    StockPriceEntity.of(
                        stockPrice, companyEntity));
            }
        }
    }
}
