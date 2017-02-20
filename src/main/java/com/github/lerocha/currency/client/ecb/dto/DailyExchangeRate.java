package com.github.lerocha.currency.client.ecb.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lerocha on 2/20/17.
 */
public class DailyExchangeRate {
    @JacksonXmlProperty(localName = "time")
    private LocalDate date;
    @JacksonXmlProperty(localName = "Cube")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<CurrencyExchangeRate> currencyExchangeRates;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate time) {
        this.date = time;
    }

    public List<CurrencyExchangeRate> getCurrencyExchangeRates() {
        return currencyExchangeRates;
    }

    public void setCurrencyExchangeRates(List<CurrencyExchangeRate> currencyExchangeRates) {
        this.currencyExchangeRates = currencyExchangeRates;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(date)
                .toString();
    }
}
