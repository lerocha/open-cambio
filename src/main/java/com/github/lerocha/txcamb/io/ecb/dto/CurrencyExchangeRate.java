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

package com.github.lerocha.txcamb.io.ecb.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.math.BigDecimal;

/**
 * Created by lerocha on 2/20/17.
 */
public class CurrencyExchangeRate {
    @JacksonXmlProperty
    private String currency;
    @JacksonXmlProperty
    private BigDecimal rate;

    public CurrencyExchangeRate() {
    }

    public CurrencyExchangeRate(String currency, BigDecimal rate) {
        this.currency = currency;
        this.rate = rate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("currency=").append(currency)
                .append("; rate=").append(rate)
                .toString();
    }
}
