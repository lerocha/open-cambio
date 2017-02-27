package com.github.lerocha.currency.controller;

import com.github.lerocha.currency.domain.Currency;
import com.github.lerocha.currency.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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

    @GetMapping(path = "/currencies/available")
    public ResponseEntity<Resources<Currency>> getAvailableCurrencies(@RequestParam(name = "locale", required = false, defaultValue = "en_US") Locale locale) {
        List<Currency> currencies = exchangeRateService.getAvailableCurrencies(locale);
        return ResponseEntity.ok(new Resources<>(currencies, linkTo(methodOn(CurrencyController.class).getAvailableCurrencies(locale)).withSelfRel()));
    }
}
