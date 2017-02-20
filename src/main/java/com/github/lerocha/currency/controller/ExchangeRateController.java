package com.github.lerocha.currency.controller;

import com.github.lerocha.currency.dto.HistoricalExchangeRate;
import com.github.lerocha.currency.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        LocalDate localDate = safeParse(date);
        if (localDate == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(exchangeRateService.getHistoricalExchangeRate(localDate, base));
    }

    @RequestMapping(path = "/rates", method = RequestMethod.GET)
    public ResponseEntity<List<HistoricalExchangeRate>> getHistoricalExchangeRates(@RequestParam(name = "startDate", required = false) String startDate,
                                                                                   @RequestParam(name = "endDate", required = false) String endDate,
                                                                                   @RequestParam(name = "base", required = false) String base) {
        return ResponseEntity.ok(exchangeRateService.getHistoricalExchangeRates(safeParse(startDate), safeParse(endDate), base));
    }

    private static LocalDate safeParse(String date) {
        try {
            return date != null ? LocalDate.parse(date) : null;
        } catch (Exception e) {
            return null;
        }
    }
}
