package com.github.lerocha.currency.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by lerocha on 2/14/17.
 */
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UK_ExchangeRate_currency_code_exchange_date", columnNames = {"currencyCode", "exchangeDate"})
})
public class ExchangeRate extends AbstractEntity implements Serializable {

    @Column(length = 4, nullable = false)
    private String currencyCode;

    @Column(nullable = false)
    private LocalDate exchangeDate;

    @Column(precision = 19, scale = 6, nullable = false)
    private BigDecimal exchangeRate;

    public ExchangeRate() {
    }

    public ExchangeRate(LocalDate exchangeDate, String currencyCode, BigDecimal exchangeRate) {
        this.exchangeDate = exchangeDate;
        this.currencyCode = currencyCode;
        this.exchangeRate = exchangeRate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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
                .append("code=").append(currencyCode)
                .append("; date=").append(exchangeDate)
                .append("; rate=").append(exchangeRate)
                .toString();
    }
}
