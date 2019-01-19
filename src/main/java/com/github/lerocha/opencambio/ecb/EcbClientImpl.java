/*
 * Copyright 2017-2018 Luis Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.lerocha.opencambio.ecb;

import com.github.lerocha.opencambio.ecb.dto.ExchangeRatesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by lerocha on 2/20/17.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EcbClientImpl implements EcbClient {
    private final RestTemplate restTemplate;

    @Override
    public ResponseEntity<ExchangeRatesResponse> getCurrentExchangeRates() {
        try {
            final String url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
            ResponseEntity<ExchangeRatesResponse> response = restTemplate.exchange(url, HttpMethod.GET, null, ExchangeRatesResponse.class);
            log.info("getCurrentExchangeRates; status={}", response.getStatusCode());
            return response;
        } catch (Exception e) {
            log.error("getCurrentExchangeRates; status=exception", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<ExchangeRatesResponse> getLast90DaysExchangeRates() {
        try {
            final String url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml";
            ResponseEntity<ExchangeRatesResponse> response = restTemplate.exchange(url, HttpMethod.GET, null, ExchangeRatesResponse.class);
            log.info("getLast90DaysExchangeRates; status={}", response.getStatusCode());
            return response;
        } catch (Exception e) {
            log.error("getLast90DaysExchangeRates; status=exception", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<ExchangeRatesResponse> getAllExchangeRates() {
        try {
            final String url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist.xml";
            ResponseEntity<ExchangeRatesResponse> response = restTemplate.exchange(url, HttpMethod.GET, null, ExchangeRatesResponse.class);
            log.info("getAllExchangeRates; status={}", response.getStatusCode());
            return response;
        } catch (Exception e) {
            log.error("getAllExchangeRates; status=exception", e);
            throw e;
        }
    }
}
