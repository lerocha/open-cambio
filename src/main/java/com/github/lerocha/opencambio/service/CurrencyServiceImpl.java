/*
 * Copyright 2017-2024 Luis Rocha
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

package com.github.lerocha.opencambio.service;

import com.github.lerocha.opencambio.domain.Rate;
import com.github.lerocha.opencambio.ecb.EcbClient;
import com.github.lerocha.opencambio.ecb.dto.CurrencyExchangeRate;
import com.github.lerocha.opencambio.ecb.dto.DailyExchangeRate;
import com.github.lerocha.opencambio.ecb.dto.ExchangeRatesResponse;
import com.github.lerocha.opencambio.entity.Currency;
import com.github.lerocha.opencambio.entity.ExchangeRate;
import com.github.lerocha.opencambio.repository.CurrencyRepository;
import com.github.lerocha.opencambio.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MONTHS;

/**
 * Created by lerocha on 2/1/17.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private static final String SOURCE_BASE_CURRENCY = "EUR";
    private static final String TARGET_BASE_CURRENCY = "USD";

    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final EcbClient ecbClient;

    @Override
    @Cacheable(cacheNames = "currencies", unless = "#result.size()==0")
    public List<Currency> getCurrencies(Locale locale) {
        List<Currency> currencies = new ArrayList<>();
        List<Object[]> results = exchangeRateRepository.findAvailableCurrencies();
        for (Object[] result : results) {
            String currencyCode = (String) result[0];
            Currency currency = new Currency(currencyCode, java.util.Currency.getInstance(currencyCode).getDisplayName(locale != null ? locale : Locale.US));
            Date startDate = (Date) result[1];
            currency.setStartDate(startDate.toLocalDate());
            Date endDate = (Date) result[2];
            currency.setEndDate(endDate.toLocalDate());
            currencies.add(currency);
        }
        log.info("getCurrencies; locale={}; total={}", locale, currencies.size());
        return currencies.stream().sorted(Comparator.comparing(Currency::getCode)).collect(Collectors.toList());
    }

    @Override
    @Cacheable(cacheNames = "currencies")
    public Currency getCurrency(String code, Locale locale) {
        Currency currency = currencyRepository.findById(code)
                .orElseThrow(() -> new IllegalArgumentException("Invalid currency code"));
        currency.setDisplayName(java.util.Currency.getInstance(code).getDisplayName(locale != null ? locale : Locale.US));
        log.info("getCurrency; locale={}", locale);
        return currency;
    }

    @Override
    @Cacheable(cacheNames = "rates", unless = "#result.getTotalElements()==0")
    public Page<Rate> getCurrencyRates(String code, LocalDate startDate, LocalDate endDate, Integer offset) {
        Assert.notNull(code, "currency code is required");

        LocalDate minDate = exchangeRateRepository.findMinExchangeDate();
        if (startDate == null || startDate.isBefore(minDate)) {
            startDate = minDate;
        }

        LocalDate maxDate = exchangeRateRepository.findMaxExchangeDate();
        if (endDate == null || endDate.isAfter(maxDate)) {
            endDate = maxDate;
        }

        // Calculate month pagination.
        int months = (int) MONTHS.between(startDate.withDayOfMonth(1), endDate.plusMonths(1).withDayOfMonth(1).minusDays(1)) + 1;

        if (offset == null || offset < 0) {
            offset = 0;
        } else if (offset >= months) {
            offset = months - 1;
        }

        LocalDate pageStart = endDate.minusMonths(offset).withDayOfMonth(1);
        if (pageStart.isBefore(startDate)) {
            pageStart = startDate;
        }

        LocalDate pageEnd = pageStart.plusMonths(1).withDayOfMonth(1).minusDays(1);
        if (pageEnd.isAfter(endDate)) {
            pageEnd = endDate;
        }

        // Get exchange rates from the database.
        List<ExchangeRate> allExchangeRates = exchangeRateRepository.findByExchangeDateBetweenOrderByExchangeDateDesc(pageStart, pageEnd);

        // Group them by day and transform into a List of Rate objects.
        List<ExchangeRate> dailyExchangeRates = new ArrayList<>();
        List<Rate> rates = new ArrayList<>();
        LocalDate date = null;
        for (ExchangeRate exchangeRate : allExchangeRates) {
            if (date == null) {
                date = exchangeRate.getExchangeDate();
            } else if (!date.equals(exchangeRate.getExchangeDate())) {
                rates.add(createRate(code, date, dailyExchangeRates));
                dailyExchangeRates.clear();
                date = exchangeRate.getExchangeDate();
            }
            dailyExchangeRates.add(exchangeRate);
        }
        if (!dailyExchangeRates.isEmpty()) {
            rates.add(createRate(code, date, dailyExchangeRates));
        }

        Pageable pageable = PageRequest.of(offset, rates.size());
        Page<Rate> page = new PageImpl<>(rates, pageable, (long) months * rates.size());
        log.info("getCurrencyRates; code={}; startDate={}; endDate={}; total={}; offset={}; totalPages={}",
                code, startDate, endDate, rates.size(), offset, page.getTotalPages());
        return page;
    }

    private Rate createRate(String code, LocalDate date, List<ExchangeRate> exchangeRates) {
        Map<String, BigDecimal> ratesByCurrency = new LinkedHashMap<>();
        if (exchangeRates != null) {
            List<ExchangeRate> sortedRates = exchangeRates.stream()
                    .sorted(Comparator.comparing(o -> o.getCurrency().getCode()))
                    .toList();

            BigDecimal baseRate = null;
            for (ExchangeRate exchangeRate : sortedRates) {
                ratesByCurrency.put(exchangeRate.getCurrency().getCode(), exchangeRate.getExchangeRate());
                if (exchangeRate.getCurrency().getCode().equalsIgnoreCase(code)) {
                    baseRate = exchangeRate.getExchangeRate();
                }
            }

            if (!sortedRates.isEmpty() && baseRate == null) {
                throw new IllegalArgumentException("Invalid currency code");
            }

            for (Map.Entry<String, BigDecimal> entry : ratesByCurrency.entrySet()) {
                entry.setValue(entry.getValue().divide(baseRate, baseRate.scale(), RoundingMode.CEILING));
            }
        }
        return new Rate(date, code, ratesByCurrency);
    }

    @Override
    @Cacheable(cacheNames = "rates")
    public Rate getCurrencyRatesByDate(String code, LocalDate date) {
        Assert.notNull(code, "currency code is required");
        Assert.notNull(date, "date is required");
        // Get the last 7 days in case requested date falls in a non-business day.
        List<ExchangeRate> allExchangeRates = exchangeRateRepository.findByExchangeDateBetweenOrderByExchangeDateDesc(date.minusDays(7), date);
        LocalDate availableDate = !allExchangeRates.isEmpty() ? allExchangeRates.get(0).getExchangeDate() : date;
        List<ExchangeRate> exchangeRates = allExchangeRates.stream()
                .filter(exchangeRate -> exchangeRate.getExchangeDate().equals(availableDate))
                .toList();
        log.info("getCurrencyRatesByDate; code={}; requestedDate={}; availableDate={}", code, date, availableDate);
        return createRate(code, availableDate, exchangeRates);
    }

    @Override
    @Cacheable(cacheNames = "rates")
    public Rate getLatestCurrencyRates(String code) {
        LocalDate date = exchangeRateRepository.findMaxExchangeDate();
        if (date == null) {
            return null;
        }
        return getCurrencyRatesByDate(code, date);
    }

    @Override
    @CacheEvict(cacheNames = {"rates", "currencies"}, allEntries = true)
    public List<ExchangeRate> refreshExchangeRates() {
        LocalDate lastRefresh = exchangeRateRepository.findMaxExchangeDate();
        log.info("refreshExchangeRates; status=starting; lastRefresh={}", lastRefresh);

        // Initialize currency table.
        List<Currency> currencies = currencyRepository.findAll();
        if (currencies.isEmpty()) {
            currencies = java.util.Currency.getAvailableCurrencies().stream()
                    .sorted(Comparator.comparing(java.util.Currency::getCurrencyCode))
                    .map(o -> new Currency(o.getCurrencyCode(), o.getDisplayName()))
                    .collect(Collectors.toList());
            currencies = currencyRepository.saveAll(currencies);
        }

        Map<String, Currency> currencyMap = currencies.stream()
                .collect(Collectors.toMap(Currency::getCode, o -> o));

        ResponseEntity<ExchangeRatesResponse> response;
        if (lastRefresh == null || lastRefresh.isBefore(LocalDate.now().minusDays(90))) {
            // Full refresh if more than 90 days since last refresh.
            response = ecbClient.getAllExchangeRates();
        } else if (lastRefresh.isBefore(LocalDate.now().minusDays(1))) {
            // Partial refresh if less than 90 days since last refresh.
            response = ecbClient.getLast90DaysExchangeRates();
        } else {
            // Daily refresh.
            response = ecbClient.getCurrentExchangeRates();
        }
        if (!response.getStatusCode().is2xxSuccessful() ||
                response.getBody() == null ||
                response.getBody().getDailyExchangeRates() == null) {
            log.error("refreshExchangeRates; status={}; body={}", response.getStatusCode(), response.getBody());
            return null;
        }

        // Filter and sort results.
        List<DailyExchangeRate> dailyExchangeRates = response.getBody().getDailyExchangeRates().stream()
                .filter(o -> lastRefresh == null || o.getDate().isAfter(lastRefresh))
                .sorted(Comparator.comparing(DailyExchangeRate::getDate))
                .toList();

        // Convert into entity objects.
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        log.info("refreshExchangeRates; status=saving; days={}", dailyExchangeRates.size());
        for (DailyExchangeRate dailyExchangeRate : dailyExchangeRates) {
            List<CurrencyExchangeRate> currencyExchangeRates = dailyExchangeRate.getCurrencyExchangeRates();

            Optional<CurrencyExchangeRate> base = currencyExchangeRates.stream()
                    .filter(o -> o.getCurrency().equals(TARGET_BASE_CURRENCY))
                    .findFirst();

            if (base.isPresent()) {
                BigDecimal baseRate = base.get().getRate();
                // Add the base currency with exchange rate = 1.0 since it is not part of the response.
                currencyExchangeRates.add(new CurrencyExchangeRate(SOURCE_BASE_CURRENCY, BigDecimal.ONE.setScale(6, RoundingMode.HALF_UP)));

                List<ExchangeRate> exchangeRatesPerDay = currencyExchangeRates.stream()
                        .sorted(Comparator.comparing(CurrencyExchangeRate::getCurrency))
                        .map(o -> new ExchangeRate(null,
                                currencyMap.get(o.getCurrency()),
                                dailyExchangeRate.getDate(),
                                o.getRate().divide(baseRate, 6, RoundingMode.CEILING)))
                        .collect(Collectors.toList());

                // Bulk save exchange rates.
                exchangeRateRepository.saveAll(exchangeRatesPerDay);
                exchangeRates.addAll(exchangeRatesPerDay);
            }
        }
        log.info("refreshExchangeRates; status=saved; total={}", exchangeRates.size());

        // Update currencies start and end date based on exchange rates.
        exchangeRates.forEach(exchangeRate -> {
            Currency currency = currencyMap.get(exchangeRate.getCurrency().getCode());
            if (currency.getStartDate() == null || currency.getStartDate().isAfter(exchangeRate.getExchangeDate())) {
                currency.setStartDate(exchangeRate.getExchangeDate());
            }
            if (currency.getEndDate() == null || currency.getEndDate().isBefore(exchangeRate.getExchangeDate())) {
                currency.setEndDate(exchangeRate.getExchangeDate());
            }
        });
        currencyRepository.saveAll(currencies);

        log.info("refreshExchangeRates; status=ok; startDate={}; endDate={}; totalRates={}; totalCurrencies={}",
                !exchangeRates.isEmpty() ? exchangeRates.get(0).getExchangeDate() : null,
                !exchangeRates.isEmpty() ? exchangeRates.get(exchangeRates.size() - 1).getExchangeDate() : null,
                exchangeRates.size(),
                currencies.size());
        return exchangeRates;
    }
}
