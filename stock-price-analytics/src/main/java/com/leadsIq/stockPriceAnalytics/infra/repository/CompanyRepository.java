package com.leadsIq.stockPriceAnalytics.infra.repository;

import org.springframework.data.repository.CrudRepository;

import com.leadsIq.stockPriceAnalytics.infra.repository.entity.CompanyEntity;
import java.util.Optional;

public interface CompanyRepository extends CrudRepository<CompanyEntity, Long> {
    Optional<CompanyEntity> findBySymbol(String symbol);
}
