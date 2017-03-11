package com.github.lerocha.currency.service;

import com.github.lerocha.currency.domain.ExchangeRate;
import com.github.lerocha.currency.domain.Currency;
import com.github.lerocha.currency.dto.HistoricalExchangeRate;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 * Created by lerocha on 2/1/17.
 */
public interface ExchangeRateService {
    HistoricalExchangeRate getLatestExchangeRate(String base);

    HistoricalExchangeRate getHistoricalExchangeRate(LocalDate date, String base);

    List<HistoricalExchangeRate> getHistoricalExchangeRates(LocalDate startDate, LocalDate endDate, String base);

    List<Currency> getCurrencies(Locale locale);

    Currency getCurrency(String code, Locale locale);

    List<ExchangeRate> refreshExchangeRates();
}
