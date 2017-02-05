package com.github.lerocha.currency.controller;

import com.github.lerocha.currency.domain.YearlyExchangeRate;
import com.github.lerocha.currency.repository.YearlyExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lerocha on 2/4/17.
 */
@RestController
public class CurrencyController {

    private YearlyExchangeRateRepository yearlyExchangeRateRepository;

    @Autowired
    public CurrencyController(YearlyExchangeRateRepository yearlyExchangeRateRepository) {
        this.yearlyExchangeRateRepository = yearlyExchangeRateRepository;
    }

    @RequestMapping(path = "currencies/{code}", method = RequestMethod.GET)
    public ResponseEntity<Iterable<YearlyExchangeRate>> getCurrencyRates(@PathVariable(name = "code") String code) {
        return ResponseEntity.ok(yearlyExchangeRateRepository.findByCurrencyCode(code));
    }

    @RequestMapping(path = "currencies/{code}/{year}", method = RequestMethod.GET)
    public ResponseEntity<Iterable<YearlyExchangeRate>> getCurrencyRatesByYear(@PathVariable(name = "code") String code,
                                                                               @PathVariable(name = "year") Integer year) {
        return ResponseEntity.ok(yearlyExchangeRateRepository.findByCurrencyCodeAndYear(code, year));
    }
}
