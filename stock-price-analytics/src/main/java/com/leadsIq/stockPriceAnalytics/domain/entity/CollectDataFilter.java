package com.leadsIq.stockPriceAnalytics.domain.entity;

import java.time.LocalDate;
import java.util.Set;


public record CollectDataFilter(LocalDate startDate,
                                Set<String> symbols) {
}
