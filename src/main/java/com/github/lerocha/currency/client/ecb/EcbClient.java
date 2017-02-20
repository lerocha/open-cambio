package com.github.lerocha.currency.client.ecb;

import com.github.lerocha.currency.client.ecb.dto.Envelope;
import org.springframework.http.ResponseEntity;

/**
 * Created by lerocha on 2/20/17.
 */
public interface EcbClient {
    ResponseEntity<Envelope> getCurrentExchangeRates();

    ResponseEntity<Envelope> getLast90DaysExchangeRates();

    ResponseEntity<Envelope> getAllExchangeRates();
}
