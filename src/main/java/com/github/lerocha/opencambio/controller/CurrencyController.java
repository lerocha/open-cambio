/*
 * Copyright 2017-2022 Luis Rocha
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

import com.github.lerocha.opencambio.dto.Rate;
import com.github.lerocha.opencambio.entity.Currency;
import com.github.lerocha.opencambio.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
public class CurrencyController {
    private static final String DEFAULT_LANGUAGE = "en-US";

    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Currency>>> getCurrencies(@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language) {
        Locale locale = Locale.forLanguageTag(language);
        List<EntityModel<Currency>> currencies = currencyService.getCurrencies(locale)
                .stream()
                .map(currency -> EntityModel.of(currency, linkTo(methodOn(CurrencyController.class).getCurrency(language, currency.getCode())).withSelfRel()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(currencies, linkTo(methodOn(CurrencyController.class).getCurrencies(language)).withSelfRel()));
    }

    @GetMapping(path = "{code}")
    public ResponseEntity<EntityModel<Currency>> getCurrency(@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language,
                                                             @PathVariable(name = "code") String code) {
        Locale locale = Locale.forLanguageTag(language);
        Currency currency = currencyService.getCurrency(code, locale);
        if (currency == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        WebMvcLinkBuilder builder = linkTo(methodOn(CurrencyController.class).getCurrency(language, code));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.toUri());
        EntityModel<Currency> resource = EntityModel.of(currency, builder.withSelfRel());
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @GetMapping(path = "{code}/rates")
    public ResponseEntity<CollectionModel<EntityModel<Rate>>> getCurrencyRates(@PathVariable(name = "code")
                                                                                       String code,
                                                                               @RequestParam(name = "start", required = false)
                                                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                                       LocalDate startDate,
                                                                               @RequestParam(name = "end", required = false)
                                                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                                       LocalDate endDate,
                                                                               @RequestParam(name = "page", required = false)
                                                                                       Integer offset) {
        Page<Rate> page = currencyService.getCurrencyRates(code, startDate, endDate, offset);
        List<EntityModel<Rate>> rates = page.getContent()
                .stream()
                .map(rate -> EntityModel.of(rate, linkTo(methodOn(CurrencyController.class).getCurrencyRatesByDate(code, rate.getDate())).withSelfRel()))
                .collect(Collectors.toList());

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

        return ResponseEntity.ok(CollectionModel.of(rates, links));
    }

    @GetMapping(path = "{code}/rates/{date}")
    public ResponseEntity<EntityModel<Rate>> getCurrencyRatesByDate(@PathVariable(name = "code")
                                                                            String code,
                                                                    @PathVariable(name = "date")
                                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                            LocalDate date) {
        Rate rate = currencyService.getCurrencyRatesByDate(code, date);
        WebMvcLinkBuilder builder = linkTo(methodOn(CurrencyController.class).getCurrencyRatesByDate(code, date));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.toUri());
        EntityModel<Rate> resource = EntityModel.of(rate, builder.withSelfRel());
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @GetMapping(path = "{code}/rates/latest")
    public ResponseEntity<EntityModel<Rate>> getCurrencyLatestRates(@PathVariable(name = "code") String code) {
        Rate rate = currencyService.getLatestCurrencyRates(code);
        WebMvcLinkBuilder builder = linkTo(methodOn(CurrencyController.class).getCurrencyLatestRates(code));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.toUri());
        EntityModel<Rate> resource = EntityModel.of(rate, builder.withSelfRel());
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
