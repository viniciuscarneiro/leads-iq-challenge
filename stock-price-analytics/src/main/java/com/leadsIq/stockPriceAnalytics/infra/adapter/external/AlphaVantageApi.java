package com.leadsIq.stockPriceAnalytics.infra.adapter.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AlphaVantageApi {
    @Value("${alphavantage.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Map<String, Object>> getStockData(String symbol) {
        try {
            String url = String.format(
                "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&apikey=%s&outputsize=full",
                symbol, apiKey);
            return restTemplate.getForObject(url, Map.class);
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }
}
