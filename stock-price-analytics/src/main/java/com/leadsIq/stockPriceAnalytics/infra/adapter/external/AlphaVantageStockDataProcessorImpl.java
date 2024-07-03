package com.leadsIq.stockPriceAnalytics.infra.adapter.external;

import org.springframework.stereotype.Component;

import com.leadsIq.stockPriceAnalytics.domain.entity.StockPrice;
import com.leadsIq.stockPriceAnalytics.infra.adapter.StockDataProcessor;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AlphaVantageStockDataProcessorImpl implements StockDataProcessor {
    @Override
    public Set<StockPrice> processStockData(LocalDate startDate, Object stockData) {
        var data = (Map<String, Map<String, Object>>) stockData;
        var endDate = startDate.plusWeeks(3);
        return startDate
            .datesUntil(endDate)
            .map(localDate -> localDate.format(DateTimeFormatter.ISO_DATE))
            .map(date -> buildPairOfDateAndResult(date, data))
            .filter(result -> result.getValue().isPresent())
            .map(result -> parseToStockPriceModel(data, result))
            .collect(Collectors.toUnmodifiableSet());
    }

    private static AbstractMap.SimpleEntry<String, Optional<Object>> buildPairOfDateAndResult(
        String date, Map<String, Map<String, Object>> apiResponse) {
        return new AbstractMap.SimpleEntry<>(date,
            Optional.ofNullable(apiResponse.get("Time Series (Daily)").get(date)));
    }

    private static StockPrice parseToStockPriceModel(
        Map<String, Map<String, Object>> apiResponse,
        AbstractMap.SimpleEntry<String, Optional<Object>> result) {
        var dailyResult = (HashMap) result.getValue().orElseThrow();
        return new StockPrice(
            apiResponse.get("Meta Data").get("2. Symbol").toString(),
            LocalDate.parse(result.getKey()),
            Double.parseDouble(dailyResult.get("1. open").toString()),
            Double.parseDouble(dailyResult.get("2. high").toString()),
            Double.parseDouble(dailyResult.get("3. low").toString()),
            Double.parseDouble(dailyResult.get("4. close").toString()),
            Long.parseLong(dailyResult.get("5. volume").toString()));

    }
}
