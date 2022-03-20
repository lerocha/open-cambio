/*
 * Copyright 2017-2022 Luis Rocha
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

package com.github.lerocha.opencambio.integration;

import com.github.lerocha.opencambio.ecb.EcbClient;
import com.github.lerocha.opencambio.ecb.dto.CurrencyExchangeRate;
import com.github.lerocha.opencambio.ecb.dto.DailyExchangeRate;
import com.github.lerocha.opencambio.ecb.dto.ExchangeRatesResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by lerocha on 2/20/17.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class EcbClientTests {

    @Autowired
    EcbClient ecbClient;

    @Test
    void getCurrentExchangeRate() {
        ResponseEntity<ExchangeRatesResponse> response = ecbClient.getCurrentExchangeRates();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        ExchangeRatesResponse exchangeRatesResponse = response.getBody();
        assertNotNull(exchangeRatesResponse);
        assertEquals(exchangeRatesResponse.getSubject(), "Reference rates");
        assertNotNull(exchangeRatesResponse.getSender());
        assertEquals(exchangeRatesResponse.getSender().getName(), "European Central Bank");
        assertNotNull(exchangeRatesResponse.getDailyExchangeRates());
        assertTrue(exchangeRatesResponse.getDailyExchangeRates().size() > 0);
        DailyExchangeRate dailyExchangeRate = exchangeRatesResponse.getDailyExchangeRates().get(0);
        assertNotNull(dailyExchangeRate);
        assertNotNull(dailyExchangeRate.getDate());
        List<CurrencyExchangeRate> currencyExchangeRates = dailyExchangeRate.getCurrencyExchangeRates();
        assertNotNull(currencyExchangeRates);
        assertTrue(currencyExchangeRates.size() > 0);
        assertNotNull(currencyExchangeRates.get(0).getCurrency());
        assertNotNull(currencyExchangeRates.get(0).getRate());
    }

    @Test
    void getLast90DaysExchangeRates() {
        ResponseEntity<ExchangeRatesResponse> response = ecbClient.getLast90DaysExchangeRates();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        ExchangeRatesResponse exchangeRatesResponse = response.getBody();
        assertNotNull(exchangeRatesResponse);
        assertEquals(exchangeRatesResponse.getSubject(), "Reference rates");
        assertNotNull(exchangeRatesResponse.getSender());
        assertEquals(exchangeRatesResponse.getSender().getName(), "European Central Bank");
        assertNotNull(exchangeRatesResponse.getDailyExchangeRates());
        assertTrue(exchangeRatesResponse.getDailyExchangeRates().size() > 0);
        DailyExchangeRate dailyExchangeRate = exchangeRatesResponse.getDailyExchangeRates().get(0);
        assertNotNull(dailyExchangeRate);
        assertNotNull(dailyExchangeRate.getDate());
        List<CurrencyExchangeRate> currencyExchangeRates = dailyExchangeRate.getCurrencyExchangeRates();
        assertNotNull(currencyExchangeRates);
        assertTrue(currencyExchangeRates.size() > 0);
        assertNotNull(currencyExchangeRates.get(0).getCurrency());
        assertNotNull(currencyExchangeRates.get(0).getRate());
    }
}
