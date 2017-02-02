package com.github.lerocha.client.ofx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by lrocha3 on 2/1/17.
 */
@Service
public class OfxClientImpl implements OfxClient {

    private RestTemplate restTemplate;

    @Autowired
    public OfxClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public HistoricalExchangeRate getHistoricalExchangeRates(String fromCurrencyCode, String toCurrencyCode, ReportingPeriod reportingPeriod, int decimalPlaces, Frequency frequency) throws RestClientException {
        // https://api.ofx.com/PublicSite.ApiService/SpotRateHistory/5year/USD/BRL?DecimalPlaces=4&ReportingInterval=yearly
        String url = String.format("https://api.ofx.com/PublicSite.ApiService/SpotRateHistory/%s/%s/%s?DecimalPlaces=%d&ReportingInterval=%s",
                reportingPeriod.getValue(), fromCurrencyCode, toCurrencyCode, decimalPlaces, frequency.toString().toLowerCase());
        HistoricalExchangeRate historicalExchangeRate = restTemplate.getForObject(url.toString(), HistoricalExchangeRate.class);
        historicalExchangeRate.setFromCurrencyCode(fromCurrencyCode);
        historicalExchangeRate.setToCurrencyCode(toCurrencyCode);
        return historicalExchangeRate;
    }
}
