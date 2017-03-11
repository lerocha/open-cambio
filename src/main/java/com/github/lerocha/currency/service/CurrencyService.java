package com.github.lerocha.currency.service;

import com.github.lerocha.currency.domain.Currency;
import com.github.lerocha.currency.domain.ExchangeRate;
import com.github.lerocha.currency.dto.Rate;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 * Created by lerocha on 2/1/17.
 */
public interface CurrencyService {
    List<Currency> getCurrencies(Locale locale);

    Currency getCurrency(String code, Locale locale);

    List<Rate> getCurrencyRates(LocalDate startDate, LocalDate endDate, String base);

    Rate getCurrencyRatesByDate(LocalDate date, String base);

    Rate getLatestCurrencyRates(String base);

    List<ExchangeRate> refreshExchangeRates();
}
