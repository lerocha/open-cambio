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

    List<Rate> getCurrencyRates(String code, LocalDate startDate, LocalDate endDate);

    Rate getCurrencyRatesByDate(String code, LocalDate date);

    Rate getLatestCurrencyRates(String base);

    List<ExchangeRate> refreshExchangeRates();
}
