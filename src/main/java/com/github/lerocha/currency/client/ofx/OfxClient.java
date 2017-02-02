package com.github.lerocha.currency.client.ofx;

/**
 * Created by lrocha3 on 2/1/17.
 */
public interface OfxClient {
    HistoricalExchangeRate getHistoricalExchangeRates(String fromCurrencyCode, String toCurrencyCode, ReportingPeriod reportingPeriod, int decimalPlaces, Frequency frequency);
}
