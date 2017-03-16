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

package com.github.lerocha.currency;

import com.github.lerocha.currency.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by lerocha on 2/20/17.
 */
@Component
public class ExchangeRateCLR implements CommandLineRunner {

    private final CurrencyService currencyService;

    @Autowired
    public ExchangeRateCLR(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public void run(String... strings) throws Exception {
        currencyService.refreshExchangeRates();
    }
}
