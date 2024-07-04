package com.leadsIq.stockPriceAnalytics.infra.adapter.external.alphavantage;

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

    private static final String META_DATA_KEY = "Meta Data";
    private static final String SYMBOL_KEY = "2. Symbol";
    private static final String TIME_SERIES_KEY = "Time Series (Daily)";
    private static final String OPEN_KEY = "1. open";
    private static final String HIGH_KEY = "2. high";
    private static final String LOW_KEY = "3. low";
    private static final String CLOSE_KEY = "4. close";
    private static final String VOLUME_KEY = "5. volume";

    private static AbstractMap.SimpleEntry<String, Optional<Object>> buildPairOfDateAndResult(
        String date, Map<String, Map<String, Object>> apiResponse) {
        return new AbstractMap.SimpleEntry<>(date,
            Optional.ofNullable(apiResponse.get(TIME_SERIES_KEY).get(date)));
    }

    private static StockPrice parseToStockPriceModel(
        Map<String, Map<String, Object>> apiResponse,
        AbstractMap.SimpleEntry<String, Optional<Object>> result) {
        var dailyResult = (HashMap) result.getValue().orElseThrow();
        return new StockPrice(
            apiResponse.get(META_DATA_KEY).get(SYMBOL_KEY).toString(),
            LocalDate.parse(result.getKey()),
            Double.parseDouble(dailyResult.get(OPEN_KEY).toString()),
            Double.parseDouble(dailyResult.get(HIGH_KEY).toString()),
            Double.parseDouble(dailyResult.get(LOW_KEY).toString()),
            Double.parseDouble(dailyResult.get(CLOSE_KEY).toString()),
            Long.parseLong(dailyResult.get(VOLUME_KEY).toString()));

    }

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


}
