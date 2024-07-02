package com.leadsIq.stockPriceAnalytics.infra.repository;

import org.springframework.data.repository.CrudRepository;

import com.leadsIq.stockPriceAnalytics.infra.repository.entity.StockPriceEntity;
import java.time.LocalDate;
import java.util.Optional;

public interface StockPriceRepository extends CrudRepository<StockPriceEntity, Long> {
    Optional<StockPriceEntity> findByCompanyEntitySymbolAndDate(String symbol, LocalDate date);
}
