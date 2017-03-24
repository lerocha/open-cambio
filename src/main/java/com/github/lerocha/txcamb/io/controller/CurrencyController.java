/*
 * Copyright 2017 Luis Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.lerocha.txcamb.io.controller;

import com.github.lerocha.txcamb.io.domain.Currency;
import com.github.lerocha.txcamb.io.dto.Rate;
import com.github.lerocha.txcamb.io.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MONTHS;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by lerocha on 2/24/17.
 */
@RestController
@RequestMapping(path = "v1/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public ResponseEntity<Resources<Resource<Currency>>> getCurrencies(HttpServletRequest request) {
        Locale locale = getLocaleFromRequest(request);
        List<Resource<Currency>> currencies = currencyService.getCurrencies(locale)
                .stream()
                .map(currency -> new Resource<>(currency, linkTo(methodOn(CurrencyController.class).getCurrency(request, currency.getCode())).withSelfRel()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new Resources<>(currencies, linkTo(methodOn(CurrencyController.class).getCurrencies(request)).withSelfRel()));
    }

    @GetMapping(path = "{code}")
    public ResponseEntity<Resource<Currency>> getCurrency(HttpServletRequest request,
                                                          @PathVariable(name = "code") String code) {
        Locale locale = getLocaleFromRequest(request);
        Currency currency = currencyService.getCurrency(code, locale);
        if (currency == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ControllerLinkBuilder builder = linkTo(methodOn(CurrencyController.class).getCurrency(request, code));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.toUri());
        Resource<Currency> resource = new Resource<>(currency, builder.withSelfRel());
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @GetMapping(path = "{code}/rates")
    public ResponseEntity<Resources<Resource<Rate>>> getCurrencyRates(@PathVariable(name = "code") String code,
                                                                      @RequestParam(name = "startDate", required = false) String startDate,
                                                                      @RequestParam(name = "endDate", required = false) String endDate,
                                                                      @RequestParam(name = "page", defaultValue = "0", required = false) int page) {
        LocalDate localDateStart = safeParse(startDate, LocalDate.of(1999, 1, 1));
        LocalDate localDateEnd = safeParse(endDate, LocalDate.now());
        if (localDateStart.isAfter(localDateEnd)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Calculate month pagination.
        int months = (int) MONTHS.between(localDateStart, localDateEnd);
        // if not the first page, then adjust start as the first day of the month of this page.
        LocalDate start = (page > 0) ? localDateStart.plusMonths(page).withDayOfMonth(1) : localDateStart;
        // if not the last page, then adjust end as the last day of the month of this page.
        LocalDate end = (page < months) ? start.plusMonths(1).withDayOfMonth(1).minusDays(1) : localDateEnd;

        List<Resource<Rate>> rates = currencyService.getCurrencyRates(code, start, end)
                .stream()
                .map(rate -> new Resource<>(rate, linkTo(methodOn(CurrencyController.class).getCurrencyRatesByDate(code, rate.getDate().toString())).withSelfRel()))
                .collect(Collectors.toList());

        // Create HATEOS links
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(CurrencyController.class).getCurrencyRates(code, localDateStart.toString(), localDateEnd.toString(), page)).withSelfRel());
        links.add(linkTo(methodOn(CurrencyController.class).getCurrencyRates(code, localDateStart.toString(), localDateEnd.toString(), 0)).withRel("first"));
        links.add(linkTo(methodOn(CurrencyController.class).getCurrencyRates(code, localDateStart.toString(), localDateEnd.toString(), months)).withRel("last"));
        if (page > 0) {
            links.add(linkTo(methodOn(CurrencyController.class).getCurrencyRates(code, localDateStart.toString(), localDateEnd.toString(), page - 1)).withRel("prev"));
        }
        if (page < months) {
            links.add(linkTo(methodOn(CurrencyController.class).getCurrencyRates(code, localDateStart.toString(), localDateEnd.toString(), page + 1)).withRel("next"));
        }

        return ResponseEntity.ok(new Resources<>(rates, links));
    }

    @GetMapping(path = "{code}/rates/{date}")
    public ResponseEntity<Resource<Rate>> getCurrencyRatesByDate(@PathVariable(name = "code") String code,
                                                                 @PathVariable(name = "date") String date) {
        LocalDate localDate = safeParse(date, null);
        if (localDate == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Rate rate = currencyService.getCurrencyRatesByDate(code, localDate);
        ControllerLinkBuilder builder = linkTo(methodOn(CurrencyController.class).getCurrencyRatesByDate(date, code));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.toUri());
        Resource<Rate> resource = new Resource(rate, builder.withSelfRel());
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @GetMapping(path = "{code}/rates/latest")
    public ResponseEntity<Resource<Rate>> getCurrencyLatestRates(@PathVariable(name = "code") String code) {
        Rate rate = currencyService.getLatestCurrencyRates(code);
        ControllerLinkBuilder builder = linkTo(methodOn(CurrencyController.class).getCurrencyLatestRates(code));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.toUri());
        Resource<Rate> resource = new Resource(rate, builder.withSelfRel());
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    private static Locale getLocaleFromRequest(HttpServletRequest request) {
        String acceptLanguage = request.getHeader("Accept-Language");
        if (acceptLanguage != null) {
            return Locale.forLanguageTag(acceptLanguage);
        }
        return null;
    }

    private static LocalDate safeParse(String date, LocalDate defaultDate) {
        try {
            return date != null ? LocalDate.parse(date) : defaultDate;
        } catch (Exception e) {
            return defaultDate;
        }
    }
}
