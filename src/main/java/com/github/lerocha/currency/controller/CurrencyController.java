package com.github.lerocha.currency.controller;

import com.github.lerocha.currency.domain.Currency;
import com.github.lerocha.currency.dto.Rate;
import com.github.lerocha.currency.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by lerocha on 2/24/17.
 */
@RestController
@RequestMapping(path = "v1/currencies")
public class CurrencyController {

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public CurrencyController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping
    public ResponseEntity<Resources<Currency>> getAvailableCurrencies(@RequestParam(name = "locale", required = false) Locale locale) {
        List<Currency> currencies = exchangeRateService.getCurrencies(locale);
        return ResponseEntity.ok(new Resources<>(currencies, linkTo(methodOn(CurrencyController.class).getAvailableCurrencies(locale)).withSelfRel()));
    }

    @GetMapping(path = "{code}")
    public ResponseEntity<Resource<Currency>> getCurrency(@PathVariable(name = "code") String code,
                                                          @RequestParam(name = "locale", required = false) Locale locale) {
        Currency currency = exchangeRateService.getCurrency(code, locale);
        if (currency == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ControllerLinkBuilder builder = linkTo(methodOn(CurrencyController.class).getCurrency(code, locale));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.toUri());
        Resource<Currency> resource = new Resource<>(currency, builder.withSelfRel());
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @GetMapping(path = "{code}/rates")
    public ResponseEntity<Resources<Rate>> getCurrencyRates(@PathVariable(name = "code") String code,
                                                            @RequestParam(name = "startDate", required = false) String startDate,
                                                            @RequestParam(name = "endDate", required = false) String endDate) {
        LocalDate localDateStart = safeParse(startDate);
        LocalDate localDateEnd = safeParse(endDate);
        List<Rate> rates = exchangeRateService.getCurrencyRates(localDateStart, localDateEnd, code);
        ControllerLinkBuilder builder = linkTo(methodOn(CurrencyController.class).getCurrencyRates(startDate, endDate, code));
        Resources<Rate> resources = new Resources<>(rates, builder.withSelfRel());
        return ResponseEntity.ok(resources);
    }

    @GetMapping(path = "{code}/rates/{date}")
    public ResponseEntity<Resource<Rate>> getCurrencyRatesByDate(@PathVariable(name = "code") String code,
                                                                 @PathVariable(name = "date") String date) {
        LocalDate localDate = safeParse(date);
        if (localDate == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Rate rate = exchangeRateService.getCurrencyRatesByDate(localDate, code);
        ControllerLinkBuilder builder = linkTo(methodOn(CurrencyController.class).getCurrencyRatesByDate(date, code));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.toUri());
        Resource<Rate> resource = new Resource(rate, builder.withSelfRel());
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @GetMapping(path = "{code}/rates/latest")
    public ResponseEntity<Resource<Rate>> getCurrencyLatestRates(@PathVariable(name = "code") String code) {
        Rate rate = exchangeRateService.getLatestCurrencyRates(code);
        ControllerLinkBuilder builder = linkTo(methodOn(CurrencyController.class).getCurrencyLatestRates(code));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.toUri());
        Resource<Rate> resource = new Resource(rate, builder.withSelfRel());
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    private static LocalDate safeParse(String date) {
        try {
            return date != null ? LocalDate.parse(date) : null;
        } catch (Exception e) {
            return null;
        }
    }
}
