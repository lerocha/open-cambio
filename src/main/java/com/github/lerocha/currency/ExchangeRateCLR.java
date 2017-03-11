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
