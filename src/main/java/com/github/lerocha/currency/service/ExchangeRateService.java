package com.github.lerocha.currency.service;

import com.github.lerocha.currency.domain.ExchangeRate;
import com.github.lerocha.currency.domain.Currency;
import com.github.lerocha.currency.dto.Rate;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 * Created by lerocha on 2/1/17.
 */
public interface ExchangeRateService {
    Rate getLatestCurrencyRates(String base);

    Rate getCurrencyRatesByDate(LocalDate date, String base);

    List<Rate> getCurrencyRates(LocalDate startDate, LocalDate endDate, String base);

    List<Currency> getCurrencies(Locale locale);

    Currency getCurrency(String code, Locale locale);

    List<ExchangeRate> refreshExchangeRates();
}
