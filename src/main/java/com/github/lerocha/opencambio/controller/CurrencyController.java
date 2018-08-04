/*
 * Copyright 2017-2018 Luis Rocha
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
    public ResponseEntity<Resources<Resource<Rate>>> getCurrencyRates(@PathVariable(name = "code")
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
        List<Resource<Rate>> rates = page.getContent()
                .stream()
                .map(rate -> new Resource<>(rate, linkTo(methodOn(CurrencyController.class).getCurrencyRatesByDate(code, rate.getDate())).withSelfRel()))
                .collect(Collectors.toList());

        // Create HATEOS links
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(CurrencyController.class).getCurrencyRates(code, startDate, endDate, offset)).withSelfRel());
        links.add(linkTo(methodOn(CurrencyController.class).getCurrencyRates(code, startDate, endDate, 0)).withRel(Link.REL_FIRST));
        links.add(linkTo(methodOn(CurrencyController.class).getCurrencyRates(code, startDate, endDate, page.getTotalPages() - 1)).withRel(Link.REL_LAST));
        if (page.hasPrevious()) {
            links.add(linkTo(methodOn(CurrencyController.class).getCurrencyRates(code, startDate, endDate, page.getNumber() - 1)).withRel(Link.REL_PREVIOUS));
        }
        if (page.hasNext()) {
            links.add(linkTo(methodOn(CurrencyController.class).getCurrencyRates(code, startDate, endDate, page.getNumber() + 1)).withRel(Link.REL_NEXT));
        }

        return ResponseEntity.ok(new Resources<>(rates, links));
    }

    @GetMapping(path = "{code}/rates/{date}")
    public ResponseEntity<Resource<Rate>> getCurrencyRatesByDate(@PathVariable(name = "code")
                                                                         String code,
                                                                 @PathVariable(name = "date")
                                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                         LocalDate date) {
        Rate rate = currencyService.getCurrencyRatesByDate(code, date);
        ControllerLinkBuilder builder = linkTo(methodOn(CurrencyController.class).getCurrencyRatesByDate(code, date));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.toUri());
        Resource<Rate> resource = new Resource<>(rate, builder.withSelfRel());
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @GetMapping(path = "{code}/rates/latest")
    public ResponseEntity<Resource<Rate>> getCurrencyLatestRates(@PathVariable(name = "code") String code) {
        Rate rate = currencyService.getLatestCurrencyRates(code);
        ControllerLinkBuilder builder = linkTo(methodOn(CurrencyController.class).getCurrencyLatestRates(code));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.toUri());
        Resource<Rate> resource = new Resource<>(rate, builder.withSelfRel());
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    private static Locale getLocaleFromRequest(HttpServletRequest request) {
        String acceptLanguage = request.getHeader("Accept-Language");
        if (acceptLanguage != null) {
            return Locale.forLanguageTag(acceptLanguage);
        }
        return null;
    }
}
