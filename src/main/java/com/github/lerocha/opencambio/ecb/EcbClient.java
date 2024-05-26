/*
 * Copyright 2017-2024 Luis Rocha
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
import org.springframework.http.ResponseEntity;

/**
 * Created by lerocha on 2/20/17.
 */
public interface EcbClient {
    ResponseEntity<ExchangeRatesResponse> getCurrentExchangeRates();

    ResponseEntity<ExchangeRatesResponse> getLast90DaysExchangeRates();

    ResponseEntity<ExchangeRatesResponse> getAllExchangeRates();
}
