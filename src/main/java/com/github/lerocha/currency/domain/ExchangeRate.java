package com.github.lerocha.currency.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by lerocha on 2/14/17.
 */
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UK_ExchangeRate_currency_id_exchange_date", columnNames = {"currency_code", "exchangeDate"})
})
public class ExchangeRate extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Currency currency;

    @Column(nullable = false)
    private LocalDate exchangeDate;

    @Column(precision = 19, scale = 6, nullable = false)
    private BigDecimal exchangeRate;

    public ExchangeRate() {
    }

    public ExchangeRate(LocalDate exchangeDate, Currency currency, BigDecimal exchangeRate) {
        this.exchangeDate = exchangeDate;
        this.currency = currency;
        this.exchangeRate = exchangeRate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public LocalDate getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(LocalDate exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("code=").append(currency != null ? currency.getCode() : null)
                .append("; date=").append(exchangeDate)
                .append("; rate=").append(exchangeRate)
                .toString();
    }
}
