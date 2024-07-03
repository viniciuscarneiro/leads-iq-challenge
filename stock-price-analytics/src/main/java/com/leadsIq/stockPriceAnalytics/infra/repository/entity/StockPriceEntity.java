package com.leadsIq.stockPriceAnalytics.infra.repository.entity;

import com.leadsIq.stockPriceAnalytics.domain.entity.StockPrice;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Data
@Table(name = "stock_price")
public class StockPriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyEntity companyEntity;

    @Column(nullable = false, unique = true)
    private LocalDate date;

    private Double open;
    private Double close;
    private Double high;
    private Double low;
    private Long volume;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static StockPriceEntity of(StockPrice stockPriceModel, CompanyEntity companyEntity) {
        StockPriceEntity stockPriceEntity = new StockPriceEntity();
        stockPriceEntity.setCompanyEntity(companyEntity);
        stockPriceEntity.setClose(stockPriceModel.close());
        stockPriceEntity.setHigh(stockPriceModel.high());
        stockPriceEntity.setLow(stockPriceModel.low());
        stockPriceEntity.setOpen(stockPriceModel.open());
        stockPriceEntity.setVolume(stockPriceModel.volume());
        stockPriceEntity.setDate(stockPriceModel.date());
        stockPriceEntity.setCreatedAt(LocalDateTime.now());
        return stockPriceEntity;
    }

    public StockPrice toDomain() {
        return new StockPrice(
            this.companyEntity.getSymbol(), this.date, this.open, this.close,
            this.high, this.low, this.volume);
    }
}
