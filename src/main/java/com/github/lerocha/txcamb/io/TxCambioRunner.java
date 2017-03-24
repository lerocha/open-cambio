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

package com.github.lerocha.txcamb.io;

import com.github.lerocha.txcamb.io.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Created by lerocha on 2/20/17.
 */
@Component
public class TxCambioRunner implements ApplicationRunner {

    private final CurrencyService currencyService;

    @Autowired
    public TxCambioRunner(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        currencyService.refreshExchangeRates();
    }
}
