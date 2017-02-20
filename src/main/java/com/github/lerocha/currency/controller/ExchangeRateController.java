package com.github.lerocha.currency.controller;

import com.github.lerocha.currency.dto.HistoricalExchangeRate;
import com.github.lerocha.currency.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @RequestMapping(path = "/rates/latest", method = RequestMethod.GET)
    public ResponseEntity<HistoricalExchangeRate> getLatestExchangeRate(@RequestParam(name = "base", required = false) String base) {
        return ResponseEntity.ok(exchangeRateService.getLatestExchangeRate(base));
    }

    @RequestMapping(path = "/rates/{date}", method = RequestMethod.GET)
    public ResponseEntity<HistoricalExchangeRate> getHistoricalExchangeRate(@PathVariable(name = "date") String date,
                                                                            @RequestParam(name = "base", required = false) String base) {
        return ResponseEntity.ok(exchangeRateService.getHistoricalExchangeRate(LocalDate.parse(date), base));
    }

    @RequestMapping(path = "/rates/period", method = RequestMethod.GET)
    public ResponseEntity<List<HistoricalExchangeRate>> getHistoricalExchangeRates(@RequestParam(name = "startDate") String startDate,
                                                                                   @RequestParam(name = "endDate") String endDate,
                                                                                   @RequestParam(name = "base", required = false) String base) {
        return ResponseEntity.ok(exchangeRateService.getHistoricalExchangeRates(LocalDate.parse(startDate), LocalDate.parse(endDate), base));
    }
}
