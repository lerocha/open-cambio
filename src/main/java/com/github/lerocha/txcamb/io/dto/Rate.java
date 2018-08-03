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

package com.github.lerocha.txcamb.io.dto;

import com.github.lerocha.txcamb.io.entity.ExchangeRate;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lerocha on 2/14/17.
 */
@Data
public class Rate implements Serializable {
    private static final long serialVersionUID = -6309472981222351233L;

    private LocalDate date;
    private String base;
    private Map<String, BigDecimal> rates = new LinkedHashMap<>();

    public Rate(String code, LocalDate date, List<ExchangeRate> rates) {
        this.setDate(date);
        this.setBase(code);

        if (rates != null) {
            List<ExchangeRate> sortedRates = rates.stream()
                    .sorted(Comparator.comparing(o -> o.getCurrency().getCode()))
                    .collect(Collectors.toList());

            BigDecimal baseRate = null;
            for (ExchangeRate exchangeRate : sortedRates) {
                this.getRates().put(exchangeRate.getCurrency().getCode(), exchangeRate.getExchangeRate());
                if (exchangeRate.getCurrency().getCode().equalsIgnoreCase(code)) {
                    baseRate = exchangeRate.getExchangeRate();
                }
            }

            if (baseRate != null) {
                for (Map.Entry<String, BigDecimal> entry : this.getRates().entrySet()) {
                    entry.setValue(entry.getValue().divide(baseRate, baseRate.scale(), BigDecimal.ROUND_CEILING));
                }
            }
        }
    }
}
