package com.leadsIq.stockPriceAnalytics.domain.usecase;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.leadsIq.stockPriceAnalytics.domain.entity.CollectDataFilter;
import com.leadsIq.stockPriceAnalytics.domain.gateway.DataCollectorGateway;
import java.time.LocalDate;
import java.util.Set;

public class CollectDataUseCaseTest {

    @Test
    public void testExecute() {
        DataCollectorGateway dataCollectorGateway = Mockito.mock(DataCollectorGateway.class);
        CollectDataUseCaseImpl useCase = new CollectDataUseCaseImpl(dataCollectorGateway);

        LocalDate startDate = LocalDate.of(2024, 7, 3);
        Set<String> symbols = Set.of("AAPL", "GOOGL");
        CollectDataFilter filter = new CollectDataFilter(startDate, symbols);

        useCase.execute(filter);

        Mockito.verify(dataCollectorGateway, Mockito.times(1)).collectStockData(filter);
    }
}
