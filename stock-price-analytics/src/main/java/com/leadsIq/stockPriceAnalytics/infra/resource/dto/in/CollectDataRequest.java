package com.leadsIq.stockPriceAnalytics.infra.resource.dto.in;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leadsIq.stockPriceAnalytics.domain.entity.CollectDataFilter;
import java.time.LocalDate;
import java.util.Set;


public record CollectDataRequest(@JsonFormat(pattern = "MM-dd-yyyy") LocalDate startDate,
                                 Set<String> symbols) {
    public CollectDataFilter toDomain() {
        return new CollectDataFilter(startDate, symbols);
    }
}
