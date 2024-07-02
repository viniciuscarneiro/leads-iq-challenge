package com.leadsIq.stockPriceAnalytics.infra.resource;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leadsIq.stockPriceAnalytics.domain.usecase.CollectDataUseCase;
import com.leadsIq.stockPriceAnalytics.infra.adapter.gateway.StockPriceGatewayImpl;
import com.leadsIq.stockPriceAnalytics.infra.resource.dto.in.CollectDataRequest;
import com.leadsIq.stockPriceAnalytics.infra.resource.dto.out.StockPriceResponse;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StockDataResource {

    private final StockPriceGatewayImpl stockDataGatewayImpl;
    private final CollectDataUseCase collectDataUseCase;

    @PostMapping("/collect_data")
    public ResponseEntity<?> collectData(@RequestBody CollectDataRequest collectDataRequest) {
        collectDataUseCase.execute(collectDataRequest.toDomain());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stock_data/{symbol}/{date}")
    public ResponseEntity<StockPriceResponse> getStockData(@PathVariable String symbol,
                                                           @PathVariable
                                                           @DateTimeFormat(pattern = "MM-dd-yyyy")
                                                           LocalDate date) {
        return ResponseEntity.ok()
            .body(StockPriceResponse.of(
                stockDataGatewayImpl.findByCompanySymbolAndDate(symbol, date)));
    }
}
