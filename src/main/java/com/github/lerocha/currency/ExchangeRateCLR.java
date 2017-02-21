package com.github.lerocha.currency;

import com.github.lerocha.currency.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by lerocha on 2/20/17.
 */
@Component
public class ExchangeRateCLR implements CommandLineRunner {

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRateCLR(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public void run(String... strings) throws Exception {
        exchangeRateService.refreshExchangeRates();
    }
}
