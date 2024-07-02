package com.leadsIq.stockPriceAnalytics.domain.usecase;

import com.leadsIq.stockPriceAnalytics.domain.entity.CollectDataFilter;

public interface CollectDataUseCase {
    void execute(CollectDataFilter collectDataFilter);
}
