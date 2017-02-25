package com.github.lerocha.currency.client.ecb.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.math.BigDecimal;

/**
 * Created by lerocha on 2/20/17.
 */
public class CurrencyExchangeRate {
    @JacksonXmlProperty
    private String currency;
    @JacksonXmlProperty
    private BigDecimal rate;

    public CurrencyExchangeRate() {
    }

    public CurrencyExchangeRate(String currency, BigDecimal rate) {
        this.currency = currency;
        this.rate = rate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("currency=").append(currency)
                .append("; rate=").append(rate)
                .toString();
    }
}
