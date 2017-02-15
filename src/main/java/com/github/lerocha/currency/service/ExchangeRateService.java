package com.github.lerocha.currency.service;

import com.github.lerocha.currency.dto.HistoricalExchangeRate;

import java.time.LocalDate;

/**
 * Created by lerocha on 2/1/17.
 */
public interface ExchangeRateService {
    HistoricalExchangeRate getHistoricalExchangeRate(LocalDate date, String base);

    void refreshAnnualExchangeRates();
}
