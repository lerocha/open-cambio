package com.github.lerocha.currency.controller;

import com.github.lerocha.currency.dto.HistoricalExchangeRate;
import com.github.lerocha.currency.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by lerocha on 2/4/17.
 */
@RestController
@ExposesResourceFor(HistoricalExchangeRate.class)
@RequestMapping("/rates")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping(path = "/latest")
    public ResponseEntity<Resource<HistoricalExchangeRate>> getLatestExchangeRate(@RequestParam(name = "base", required = false) String base) {
        HistoricalExchangeRate historicalExchangeRate = exchangeRateService.getLatestExchangeRate(base);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(ExchangeRateController.class).slash(historicalExchangeRate).toUri());
        return new ResponseEntity<>(new Resource<>(historicalExchangeRate,
                linkTo(methodOn(ExchangeRateController.class).getLatestExchangeRate(base)).withSelfRel()), headers, HttpStatus.OK);
    }

    @GetMapping(path = "/{date}")
    public ResponseEntity<Resource<HistoricalExchangeRate>> getHistoricalExchangeRate(@PathVariable(name = "date") String date,
                                                                                      @RequestParam(name = "base", required = false) String base) {
        LocalDate localDate = safeParse(date);
        if (localDate == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        HistoricalExchangeRate historicalExchangeRate = exchangeRateService.getHistoricalExchangeRate(localDate, base);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(ExchangeRateController.class).slash(historicalExchangeRate).toUri());
        return new ResponseEntity<>(new Resource<>(historicalExchangeRate,
                linkTo(methodOn(ExchangeRateController.class).getHistoricalExchangeRate(date, base)).withSelfRel()), headers, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Resources<HistoricalExchangeRate>> getHistoricalExchangeRates(@RequestParam(name = "startDate", required = false) String startDate,
                                                                                        @RequestParam(name = "endDate", required = false) String endDate,
                                                                                        @RequestParam(name = "base", required = false) String base) {
        LocalDate localDateStart = safeParse(startDate);
        LocalDate localDateEnd = safeParse(endDate);
        List<HistoricalExchangeRate> historicalExchangeRates = exchangeRateService.getHistoricalExchangeRates(localDateStart, localDateEnd, base);
        return ResponseEntity.ok(new Resources<>(historicalExchangeRates,
                linkTo(methodOn(ExchangeRateController.class).getHistoricalExchangeRates(startDate, endDate, base)).withSelfRel()));
    }

    private static LocalDate safeParse(String date) {
        try {
            return date != null ? LocalDate.parse(date) : null;
        } catch (Exception e) {
            return null;
        }
    }
}
