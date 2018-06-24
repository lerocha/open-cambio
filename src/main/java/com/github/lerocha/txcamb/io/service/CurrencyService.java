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

package com.github.lerocha.txcamb.io.service;

import com.github.lerocha.txcamb.io.entity.Currency;
import com.github.lerocha.txcamb.io.entity.ExchangeRate;
import com.github.lerocha.txcamb.io.dto.Rate;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 * Created by lerocha on 2/1/17.
 */
public interface CurrencyService {
    List<Currency> getCurrencies(Locale locale);

    Currency getCurrency(String code, Locale locale);

    List<Rate> getCurrencyRates(String code, LocalDate startDate, LocalDate endDate);

    Rate getCurrencyRatesByDate(String code, LocalDate date);

    Rate getLatestCurrencyRates(String base);

    List<ExchangeRate> refreshExchangeRates();
}
