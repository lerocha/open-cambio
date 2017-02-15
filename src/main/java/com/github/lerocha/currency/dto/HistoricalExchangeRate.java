package com.github.lerocha.currency.dto;

import org.joda.time.LocalDate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lerocha on 2/14/17.
 */
public class HistoricalExchangeRate implements Serializable {
    private LocalDate date;
    private String base;
    private Map<String, BigDecimal> rates = new HashMap<>();

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }
}
