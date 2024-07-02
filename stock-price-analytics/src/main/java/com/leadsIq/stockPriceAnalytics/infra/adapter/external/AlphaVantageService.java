package com.leadsIq.stockPriceAnalytics.infra.adapter.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.leadsIq.stockPriceAnalytics.domain.entity.StockPrice;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AlphaVantageService {
    @Value("${alphavantage.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Map<String, Object>> getStockData(String symbol) {
        try {
            // Implement API call logic here
            String url = String.format(
                "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&apikey=%s&outputsize=full",
                symbol, apiKey);
            return restTemplate.getForObject(url, Map.class);
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }

    // Process API response and return necessary data
    public Set<StockPrice> processStockData(Map<String, Map<String, Object>> apiResponse,
                                            LocalDate startDate) {
        var endDate = startDate.plusWeeks(3);
        return startDate
            .datesUntil(endDate)
            .map(localDate -> localDate.format(DateTimeFormatter.ISO_DATE))
            .map(date -> new AbstractMap.SimpleEntry<>(date, Optional.ofNullable(apiResponse.get("Time Series (Daily)").get(date))))
            .filter(result -> result.getValue().isPresent())
            .map(result -> parseToStockPriceModel(apiResponse, result))
            .collect(Collectors.toUnmodifiableSet());
    }

    private static StockPrice parseToStockPriceModel(
        Map<String, Map<String, Object>> apiResponse,
        AbstractMap.SimpleEntry<String, Optional<Object>> result) {
        var dailyResult = (HashMap) result.getValue().orElseThrow();
        return new StockPrice(
            apiResponse.get("Meta Data").get("2. Symbol").toString(),
            LocalDate.parse(result.getKey()),
            new BigDecimal(dailyResult.get("1. open").toString()),
            new BigDecimal(dailyResult.get("2. high").toString()),
            new BigDecimal(dailyResult.get("3. low").toString()),
            new BigDecimal(dailyResult.get("4. close").toString()),
            Long.parseLong(dailyResult.get("5. volume").toString()));
    }
}
