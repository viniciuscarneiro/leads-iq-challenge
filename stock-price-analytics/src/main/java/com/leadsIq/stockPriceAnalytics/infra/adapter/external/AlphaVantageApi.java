package com.leadsIq.stockPriceAnalytics.infra.adapter.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "${feign.name}", url = "${feign.url}", fallbackFactory = AlphaVantageApiFallbackFactory.class)
public interface AlphaVantageApi {

    @GetMapping("/query")
    Map<String, Map<String, Object>> getStockData(
        @RequestParam String function,
        @RequestParam String symbol,
        @RequestParam String apikey,
        @RequestParam String outputsize);
}
