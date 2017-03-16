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

package com.github.lerocha.currency.scheduled;

import com.github.lerocha.currency.service.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by lerocha on 3/7/17.
 */
@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private final CurrencyService currencyService;

    @Autowired
    public ScheduledTasks(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    /**
     * Scheduled task to refresh exchange rates.
     * ECB usually updates exchange rates around 16:00 CET on every working day, except on TARGET closing days.
     * Source: http://www.ecb.europa.eu/stats/policy_and_exchange_rates/euro_reference_exchange_rates/html/index.en.html
     */
    @Scheduled(cron = "0 30 16 * * *")
    public void refreshExchangeRatesTask() {
        try {
            currencyService.refreshExchangeRates();
            log.info("refreshExchangeRatesTask; status=completed");
        } catch (Exception e) {
            log.error("refreshExchangeRatesTask; status=failed", e);
        }
    }
}
