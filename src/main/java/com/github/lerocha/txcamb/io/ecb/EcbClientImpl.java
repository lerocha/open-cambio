/*
 * Copyright 2017 Luis Rocha
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

package com.github.lerocha.txcamb.io.ecb;

import com.github.lerocha.txcamb.io.ecb.dto.ExchangeRatesResponse;
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
