package com.github.lerocha.currency.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lerocha on 2/1/17.
 */
@Entity
public class YearlyExchangeRate extends AbstractEntity implements Serializable {

    @Column
    private String currencyCode;

    @Column
    private Integer year;

    @Column(precision = 19, scale = 6)
    private BigDecimal exchangeRate;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
