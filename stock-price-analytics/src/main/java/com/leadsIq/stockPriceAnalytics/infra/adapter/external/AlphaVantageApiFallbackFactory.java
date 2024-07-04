package com.leadsIq.stockPriceAnalytics.infra.adapter.external;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlphaVantageApiFallbackFactory implements FallbackFactory<AlphaVantageApi> {

    private final AlphaVantageApiStaticFallback alphaVantageApiStaticFallback;

    @Override
    public AlphaVantageApi create(Throwable cause) {
        log.error(cause.getMessage(), cause);
        return alphaVantageApiStaticFallback;
    }
}
