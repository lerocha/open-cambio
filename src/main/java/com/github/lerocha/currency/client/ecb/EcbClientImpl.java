package com.github.lerocha.currency.client.ecb;

import com.github.lerocha.currency.client.ecb.dto.ExchangeRatesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by lerocha on 2/20/17.
 */
@Component
public class EcbClientImpl implements EcbClient {
    private RestTemplate restTemplate;

    @Autowired
    public EcbClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<ExchangeRatesResponse> getCurrentExchangeRates() {
        final String url = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
        return restTemplate.exchange(url, HttpMethod.GET, null, ExchangeRatesResponse.class);
    }

    @Override
    public ResponseEntity<ExchangeRatesResponse> getLast90DaysExchangeRates() {
        final String url = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml";
        return restTemplate.exchange(url, HttpMethod.GET, null, ExchangeRatesResponse.class);
    }

    @Override
    public ResponseEntity<ExchangeRatesResponse> getAllExchangeRates() {
        final String url = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist.xml";
        return restTemplate.exchange(url, HttpMethod.GET, null, ExchangeRatesResponse.class);
    }
}
