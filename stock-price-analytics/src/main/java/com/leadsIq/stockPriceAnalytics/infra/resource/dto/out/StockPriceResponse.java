package com.leadsIq.stockPriceAnalytics.infra.resource.dto.out;

import com.leadsIq.stockPriceAnalytics.domain.entity.StockPrice;
import java.math.BigDecimal;
import java.time.LocalDate;

public record StockPriceResponse(String company, LocalDate date, BigDecimal open, BigDecimal close,
                                 BigDecimal high, BigDecimal low, Long volume) {

    public static StockPriceResponse of(StockPrice stockPrice) {
        return new StockPriceResponse(stockPrice.company(), stockPrice.date(),
            stockPrice.open()
            , stockPrice.close(),
            stockPrice.high(), stockPrice.low(), stockPrice.volume());
    }
}
