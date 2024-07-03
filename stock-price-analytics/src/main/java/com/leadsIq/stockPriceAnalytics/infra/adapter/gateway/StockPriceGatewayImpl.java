package com.leadsIq.stockPriceAnalytics.infra.adapter.gateway;

import static com.leadsIq.stockPriceAnalytics.domain.util.Constants.STOCK_PRICE_NOT_FOUND_ERROR_MESSAGE;

import org.springframework.stereotype.Service;

import com.leadsIq.stockPriceAnalytics.domain.entity.StockPrice;
import com.leadsIq.stockPriceAnalytics.domain.exception.EntityNotFoundException;
import com.leadsIq.stockPriceAnalytics.domain.gateway.StockPriceGateway;
import com.leadsIq.stockPriceAnalytics.infra.repository.StockPriceRepository;
import com.leadsIq.stockPriceAnalytics.infra.repository.entity.StockPriceEntity;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockPriceGatewayImpl implements StockPriceGateway {

    private final StockPriceRepository stockPriceRepository;

    @Override
    public StockPrice findByCompanySymbolAndDate(String symbol, LocalDate date) {
        return stockPriceRepository.findByCompanyEntitySymbolAndDate(symbol, date)
            .orElseThrow(() -> new EntityNotFoundException(
                String.format(STOCK_PRICE_NOT_FOUND_ERROR_MESSAGE, symbol, date)))
            .toDomain();
    }

    @Override
    public List<StockPrice> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return stockPriceRepository.findByDateBetween(startDate, endDate).stream()
            .map(StockPriceEntity::toDomain).toList();
    }
}
