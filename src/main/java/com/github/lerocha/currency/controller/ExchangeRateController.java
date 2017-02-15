package com.github.lerocha.currency.controller;

import com.github.lerocha.currency.dto.HistoricalExchangeRate;
import com.github.lerocha.currency.service.ExchangeRateService;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by lerocha on 2/4/17.
 */
@RestController
public class ExchangeRateController {

    private ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @RequestMapping(path = "rates/historical/{date}", method = RequestMethod.GET)
    public ResponseEntity<HistoricalExchangeRate> getHistoricalRates(@PathVariable(name = "date") String date,
                                                                     @RequestParam(name = "base", required = false) String base) {
        return ResponseEntity.ok(exchangeRateService.getHistoricalExchangeRate(LocalDate.parse(date), base));
    }
}
