package com.github.lerocha.currency.controller;

import com.github.lerocha.currency.domain.Currency;
import com.github.lerocha.currency.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

/**
 * Created by lerocha on 2/24/17.
 */
@RestController
public class CurrencyController {

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public CurrencyController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @RequestMapping(path = "/api/currencies")
    public ResponseEntity<List<Currency>> getAvailableCurrencies(@RequestParam(name = "locale", required = false, defaultValue = "en_US") Locale locale) {
        return ResponseEntity.ok(exchangeRateService.getAvailableCurrencies(locale));
    }
}
