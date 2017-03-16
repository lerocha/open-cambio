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

package com.github.lerocha.currency.client.ecb.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lerocha on 2/20/17.
 */
public class DailyExchangeRate {
    @JacksonXmlProperty(localName = "time")
    private LocalDate date;
    @JacksonXmlProperty(localName = "Cube")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<CurrencyExchangeRate> currencyExchangeRates;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate time) {
        this.date = time;
    }

    public List<CurrencyExchangeRate> getCurrencyExchangeRates() {
        return currencyExchangeRates;
    }

    public void setCurrencyExchangeRates(List<CurrencyExchangeRate> currencyExchangeRates) {
        this.currencyExchangeRates = currencyExchangeRates;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(date)
                .toString();
    }
}
