package com.github.lerocha.currency.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lerocha on 2/14/17.
 */
public class Rate {
    private LocalDate date;
    private String base;
    private Map<String, BigDecimal> rates = new LinkedHashMap<>();

    public Rate() {
    }

    public Rate(LocalDate date, String base) {
        this.date = date;
        this.base = base;
    }

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
