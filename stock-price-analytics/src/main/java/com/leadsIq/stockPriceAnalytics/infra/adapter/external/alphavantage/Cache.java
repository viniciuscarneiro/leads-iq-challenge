package com.leadsIq.stockPriceAnalytics.infra.adapter.external.alphavantage;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.Map;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class Cache {

    public static Map<String, Map<String, Object>> getData(String symbol) {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream("cache/%s.json".formatted(symbol))) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(inputStream, Map.class);
        } catch (Exception e) {
            log.error("Error while trying to use static cache.", e);
            throw new RuntimeException(e);
        }
    }
}
