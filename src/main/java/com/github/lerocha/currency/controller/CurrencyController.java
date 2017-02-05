package com.github.lerocha.currency.controller;

import com.github.lerocha.currency.domain.AnnualExchangeRate;
import com.github.lerocha.currency.repository.AnnualExchangeRateRepository;
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

    private AnnualExchangeRateRepository annualExchangeRateRepository;

    @Autowired
    public CurrencyController(AnnualExchangeRateRepository annualExchangeRateRepository) {
        this.annualExchangeRateRepository = annualExchangeRateRepository;
    }

    @RequestMapping(path = "currencies/{code}", method = RequestMethod.GET)
    public ResponseEntity<Iterable<AnnualExchangeRate>> getCurrencyRates(@PathVariable(name = "code") String code) {
        return ResponseEntity.ok(annualExchangeRateRepository.findByCurrencyCode(code));
    }

    @RequestMapping(path = "currencies/{code}/{year}", method = RequestMethod.GET)
    public ResponseEntity<Iterable<AnnualExchangeRate>> getCurrencyRatesByYear(@PathVariable(name = "code") String code,
                                                                               @PathVariable(name = "year") Integer year) {
        return ResponseEntity.ok(annualExchangeRateRepository.findByCurrencyCodeAndYear(code, year));
    }
}
