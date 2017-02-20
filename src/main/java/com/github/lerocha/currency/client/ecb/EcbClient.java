package com.github.lerocha.currency.client.ecb;

import com.github.lerocha.currency.client.ecb.dto.ExchangeRatesResponse;
import org.springframework.http.ResponseEntity;

/**
 * Created by lerocha on 2/20/17.
 */
public interface EcbClient {
    ResponseEntity<ExchangeRatesResponse> getCurrentExchangeRates();

    ResponseEntity<ExchangeRatesResponse> getLast90DaysExchangeRates();

    ResponseEntity<ExchangeRatesResponse> getAllExchangeRates();
}
