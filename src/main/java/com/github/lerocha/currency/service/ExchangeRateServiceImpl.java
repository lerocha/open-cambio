package com.github.lerocha.currency.service;

import com.github.lerocha.currency.client.ecb.EcbClient;
import com.github.lerocha.currency.client.ecb.dto.CurrencyExchangeRate;
import com.github.lerocha.currency.client.ecb.dto.DailyExchangeRate;
import com.github.lerocha.currency.client.ecb.dto.ExchangeRatesResponse;
import com.github.lerocha.currency.domain.Currency;
import com.github.lerocha.currency.domain.ExchangeRate;
import com.github.lerocha.currency.dto.Rate;
import com.github.lerocha.currency.repository.CurrencyRepository;
import com.github.lerocha.currency.repository.ExchangeRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lerocha on 2/1/17.
 */
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);
    private static final Currency BASE_CURRENCY = new Currency("EUR", null, LocalDate.parse("1999-01-04"), LocalDate.now());

    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final EcbClient ecbClient;

    @Autowired
    public ExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository,
                                   CurrencyRepository currencyRepository,
                                   EcbClient ecbClient) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.currencyRepository = currencyRepository;
        this.ecbClient = ecbClient;
    }

    @Override
    public Rate getLatestCurrencyRates(String base) {
        LocalDate date = exchangeRateRepository.findMaxExchangeDate();
        if (date == null) {
            return null;
        }

        return getCurrencyRatesByDate(date, base);
    }

    @Override
    public Rate getCurrencyRatesByDate(LocalDate date, String base) {
        Assert.notNull(date);
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findByExchangeDateOrderByCurrencyCode(date);
        logger.info("getCurrencyRatesByDate; date={}; base={}", date, base);
        return getCurrencyRatesByDate(date, base, exchangeRates);
    }

    private Rate getCurrencyRatesByDate(LocalDate date, String base, List<ExchangeRate> rates) {
        Assert.notNull(date);
        Assert.notNull(rates);
        Rate rate = new Rate(date, base != null ? base : BASE_CURRENCY.getCode());
        BigDecimal baseRate = null;
        for (ExchangeRate exchangeRate : rates.stream().sorted(Comparator.comparing(o -> o.getCurrency().getCode())).collect(Collectors.toList())) {
            rate.getRates().put(exchangeRate.getCurrency().getCode(), exchangeRate.getExchangeRate());
            if (exchangeRate.getCurrency().getCode().equalsIgnoreCase(base)) {
                baseRate = exchangeRate.getExchangeRate();
            }
        }

        if (baseRate != null) {
            for (Map.Entry<String, BigDecimal> entry : rate.getRates().entrySet()) {
                entry.setValue(entry.getValue().divide(baseRate, baseRate.scale(), BigDecimal.ROUND_CEILING));
            }
        }
        return rate;
    }

    @Override
    public List<Rate> getCurrencyRates(LocalDate startDate, LocalDate endDate, String base) {
        List<Rate> rates = new ArrayList<>();
        if (startDate == null) {
            startDate = exchangeRateRepository.findMinExchangeDate();
        }
        if (endDate == null) {
            endDate = exchangeRateRepository.findMaxExchangeDate();
        }
        List<ExchangeRate> allExchangeRates = exchangeRateRepository.findByExchangeDateBetweenOrderByExchangeDate(startDate, endDate);
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        LocalDate date = null;
        for (ExchangeRate exchangeRate : allExchangeRates) {
            if (date == null) {
                date = exchangeRate.getExchangeDate();
            } else if (!date.equals(exchangeRate.getExchangeDate())) {
                rates.add(getCurrencyRatesByDate(date, base, exchangeRates));
                exchangeRates.clear();
                date = exchangeRate.getExchangeDate();
            }
            exchangeRates.add(exchangeRate);
        }
        if (exchangeRates.size() > 0) {
            rates.add(getCurrencyRatesByDate(date, base, exchangeRates));
        }

        logger.info("getCurrencyRates; startDate={}; endDate={}; base={}; total={}", startDate, endDate, base, rates.size());
        return rates;
    }

    @Override
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
        logger.info("getCurrencies; locale={}; total={}", locale, currencies.size());
        return currencies.stream().sorted(Comparator.comparing(Currency::getCode)).collect(Collectors.toList());
    }

    @Override
    public Currency getCurrency(String code, Locale locale) {
        Currency currency = currencyRepository.findByCode(code);
        if (currency != null) {
            currency.setDisplayName(java.util.Currency.getInstance(code).getDisplayName(locale != null ? locale : Locale.US));
        }
        return currency;
    }

    @Override
    @Transactional
    public List<ExchangeRate> refreshExchangeRates() {
        LocalDate lastRefresh = exchangeRateRepository.findMaxExchangeDate();
        logger.info("refreshExchangeRates; status=starting; lastRefresh={}", lastRefresh);

        // Initialize currency table.
        List<Currency> currencies = currencyRepository.findAll();
        if (currencies.size() == 0) {
            currencies = java.util.Currency.getAvailableCurrencies().stream()
                    .sorted(Comparator.comparing(java.util.Currency::getCurrencyCode))
                    .map(o -> new Currency(o.getCurrencyCode(), o.getDisplayName()))
                    .collect(Collectors.toList());
            currencies = currencyRepository.save(currencies);
        }

        Map<String, Currency> currencyMap = currencies.stream()
                .collect(Collectors.toMap(o -> o.getCode(), o -> o));

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
            logger.error("refreshExchangeRates; status={}; body={}", response.getStatusCode(), response.getBody());
            return null;
        }

        // Filter and sort results.
        List<DailyExchangeRate> dailyExchangeRates = response.getBody().getDailyExchangeRates().stream()
                .filter(o -> lastRefresh == null || o.getDate().isAfter(lastRefresh))
                .sorted(Comparator.comparing(DailyExchangeRate::getDate))
                .collect(Collectors.toList());

        // Convert into entity objects.
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        for (DailyExchangeRate dailyExchangeRate : dailyExchangeRates) {
            List<CurrencyExchangeRate> currencyExchangeRates = dailyExchangeRate.getCurrencyExchangeRates();
            // Add the base currency with exchange rate = 1.0 since it is not part of the response.
            currencyExchangeRates.add(new CurrencyExchangeRate(BASE_CURRENCY.getCode(), BigDecimal.ONE.setScale(6, BigDecimal.ROUND_HALF_UP)));
            exchangeRates.addAll(currencyExchangeRates.stream()
                    .sorted(Comparator.comparing(CurrencyExchangeRate::getCurrency))
                    .map(o -> new ExchangeRate(dailyExchangeRate.getDate(), currencyMap.get(o.getCurrency()), o.getRate()))
                    .collect(Collectors.toList()));
        }

        // Bulk save exchange rates.
        exchangeRates = (List<ExchangeRate>) exchangeRateRepository.save(exchangeRates);
        int totalCurrencies = currencyRepository.updateCurrencyStartAndEndDates();
        logger.info("refreshExchangeRates; status=ok; startDate={}; endDate={}; totalRates={}; totalCurrencies={}",
                exchangeRates.size() > 0 ? exchangeRates.get(0).getExchangeDate() : null,
                exchangeRates.size() > 0 ? exchangeRates.get(exchangeRates.size() - 1).getExchangeDate() : null,
                exchangeRates.size(),
                totalCurrencies);
        return exchangeRates;
    }
}
