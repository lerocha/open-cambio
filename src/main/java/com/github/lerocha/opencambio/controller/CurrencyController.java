/*
 * Copyright 2017-2023 Luis Rocha
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

package com.github.lerocha.opencambio.controller;

import com.github.lerocha.opencambio.domain.Currency;
import com.github.lerocha.opencambio.domain.Pagination;
import com.github.lerocha.opencambio.domain.Rate;
import com.github.lerocha.opencambio.domain.RatesResponse;
import com.github.lerocha.opencambio.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Created by lerocha on 2/24/17.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "v1/currencies", produces = "application/json")
@Tag(name = "Currencies", description = "REST API for currency exchange rates from the European Central Bank")
public class CurrencyController {
    private static final String DEFAULT_LANGUAGE = "en-US";

    private final CurrencyService currencyService;

    @GetMapping
    @Operation(summary = "Get all available currencies",
            description = "Get a list of all available currencies.",
            operationId = "getCurrencies")
    public ResponseEntity<List<Currency>> getCurrencies(
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = DEFAULT_LANGUAGE)
            String language) {
        Locale locale = Locale.forLanguageTag(language);
        List<Currency> currencies = currencyService.getCurrencies(locale)
                .stream()
                .map(Currency::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(currencies);
    }

    @GetMapping(path = "{code}")
    @Operation(summary = "Get currency by currency code",
            description = "Get currency details by the currency code.",
            operationId = "getCurrency")
    public Currency getCurrency(
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = DEFAULT_LANGUAGE)
            String language,

            @Parameter(required = true, example = "USD")
            @PathVariable(name = "code")
            String code) {
        Locale locale = Locale.forLanguageTag(language);
        com.github.lerocha.opencambio.entity.Currency currency = currencyService.getCurrency(code, locale);
        return new Currency(currency);
    }

    @GetMapping(path = "{code}/rates")
    @Operation(summary = "Get exchange rates for a currency",
            description = "Get a list of exchange rates for a currency. The response is paginated and the latest currency exchanges will show first",
            operationId = "getCurrencyRates")
    public ResponseEntity<RatesResponse> getCurrencyRates(
            @PathVariable(name = "code")
            @Parameter(required = true, example = "USD")
            String code,

            @RequestParam(name = "start", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(example = "2023-01-01", description = "")
            LocalDate startDate,

            @RequestParam(name = "end", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(example = "2023-05-31", description = "")
            LocalDate endDate,

            @RequestParam(required = false)
            @Parameter(example = "0", description = "")
            Integer offset) {
        Page<Rate> page = currencyService.getCurrencyRates(code, startDate, endDate, offset);
        List<Rate> rates = page.getContent();

        // Create HATEOS links
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(CurrencyController.class).getCurrencyRates(code, startDate, endDate, offset)).withSelfRel());
        links.add(linkTo(methodOn(CurrencyController.class).getCurrencyRates(code, startDate, endDate, 0)).withRel(IanaLinkRelations.FIRST));
        links.add(linkTo(methodOn(CurrencyController.class).getCurrencyRates(code, startDate, endDate, page.getTotalPages() - 1)).withRel(IanaLinkRelations.LAST));
        if (page.hasPrevious()) {
            links.add(linkTo(methodOn(CurrencyController.class).getCurrencyRates(code, startDate, endDate, page.getNumber() - 1)).withRel(IanaLinkRelations.PREV));
        }
        if (page.hasNext()) {
            links.add(linkTo(methodOn(CurrencyController.class).getCurrencyRates(code, startDate, endDate, page.getNumber() + 1)).withRel(IanaLinkRelations.NEXT));
        }

        return ResponseEntity.ok(new RatesResponse(rates, new Pagination(page.getNumber(), page.getTotalPages()), links));
    }

    @GetMapping(path = "{code}/rates/{date}")
    @Operation(summary = "Get exchange rates for a currency in a given day",
            description = "Get a list of exchange rates for a currency in a given day.",
            operationId = "getCurrencyRatesByDate")
    public ResponseEntity<Rate> getCurrencyRatesByDate(
            @PathVariable(name = "code")
            @Parameter(required = true, example = "USD")
            String code,

            @PathVariable(name = "date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(example = "2023-06-01", description = "")
            LocalDate date) {
        Rate rate = currencyService.getCurrencyRatesByDate(code, date);
        return ResponseEntity.ok(rate);
    }

    @GetMapping(path = "{code}/rates/latest")
    @Operation(summary = "Get the latest exchange rates for a currency",
            description = "Get the latest exchange rates for a currency.",
            operationId = "getCurrencyLatestRates")
    public ResponseEntity<Rate> getCurrencyLatestRates(
            @PathVariable(name = "code")
            @Parameter(required = true, example = "USD")
            String code) {
        Rate rate = currencyService.getLatestCurrencyRates(code);
        return ResponseEntity.ok(rate);
    }
}
