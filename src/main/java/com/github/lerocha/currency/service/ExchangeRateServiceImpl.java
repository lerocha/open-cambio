package com.github.lerocha.currency.service;

import com.github.lerocha.currency.client.ofx.Frequency;
import com.github.lerocha.currency.client.ofx.HistoricalPoint;
import com.github.lerocha.currency.client.ofx.OfxClient;
import com.github.lerocha.currency.client.ofx.ReportingPeriod;
import com.github.lerocha.currency.domain.ExchangeRate;
import com.github.lerocha.currency.domain.YearlyExchangeRate;
import com.github.lerocha.currency.dto.HistoricalExchangeRate;
import com.github.lerocha.currency.repository.ExchangeRateRepository;
import com.github.lerocha.currency.repository.YearlyExchangeRateRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lerocha on 2/1/17.
 */
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);

    private ExchangeRateRepository exchangeRateRepository;
    private YearlyExchangeRateRepository yearlyExchangeRateRepository;
    private OfxClient ofxClient;

    @Autowired
    public ExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository, YearlyExchangeRateRepository yearlyExchangeRateRepository, OfxClient ofxClient) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.yearlyExchangeRateRepository = yearlyExchangeRateRepository;
        this.ofxClient = ofxClient;
    }

    private static final String DEFAULT_BASE = "EUR";

    @Override
    public HistoricalExchangeRate getLatestExchangeRate(String base) {
        LocalDate date = exchangeRateRepository.findMaxExchangeDate();
        if (date == null) {
            return null;
        }

        return getHistoricalExchangeRate(date, base);
    }

    @Override
    public HistoricalExchangeRate getHistoricalExchangeRate(LocalDate date, String base) {
        Assert.notNull(date);
        List<ExchangeRate> rates = exchangeRateRepository.findByExchangeDateOrderByCurrencyCode(date);
        return getHistoricalExchangeRate(date, base, rates);
    }

    private HistoricalExchangeRate getHistoricalExchangeRate(LocalDate date, String base, List<ExchangeRate> rates) {
        Assert.notNull(date);
        Assert.notNull(rates);
        HistoricalExchangeRate historicalExchangeRate = new HistoricalExchangeRate(date, base != null ? base : DEFAULT_BASE);
        BigDecimal baseRate = null;
        rates.add(new ExchangeRate(date, DEFAULT_BASE, BigDecimal.ONE.setScale(6, BigDecimal.ROUND_HALF_UP)));
        for (ExchangeRate rate : rates.stream().sorted(Comparator.comparing(o -> o.getCurrencyCode())).collect(Collectors.toList())) {
            historicalExchangeRate.getRates().put(rate.getCurrencyCode(), rate.getExchangeRate());
            if (rate.getCurrencyCode().equalsIgnoreCase(base)) {
                baseRate = rate.getExchangeRate();
            }
        }

        if (baseRate != null) {
            for (Map.Entry<String, BigDecimal> entry : historicalExchangeRate.getRates().entrySet()) {
                entry.setValue(entry.getValue().divide(baseRate, baseRate.scale(), BigDecimal.ROUND_CEILING));
            }
        }
        return historicalExchangeRate;
    }

    @Override
    public List<HistoricalExchangeRate> getHistoricalExchangeRates(LocalDate startDate, LocalDate endDate, String base) {
        List<HistoricalExchangeRate> historicalExchangeRates = new ArrayList<>();
        if (startDate == null) {
            startDate = exchangeRateRepository.findMinExchangeDate();
        }
        if (endDate == null) {
            endDate = exchangeRateRepository.findMaxExchangeDate();
        }
        List<ExchangeRate> allRates = exchangeRateRepository.findByExchangeDateBetweenOrderByExchangeDate(startDate, endDate);
        List<ExchangeRate> rates = new ArrayList<>();
        LocalDate date = null;
        for (ExchangeRate rate : allRates) {
            if (date == null) {
                date = rate.getExchangeDate();
            } else if (!date.equals(rate.getExchangeDate())) {
                historicalExchangeRates.add(getHistoricalExchangeRate(date, base, rates));
                rates.clear();
                date = rate.getExchangeDate();
            }
            rates.add(rate);
        }
        if (rates.size() > 0) {
            historicalExchangeRates.add(getHistoricalExchangeRate(date, base, rates));
        }

        return historicalExchangeRates;
    }

    @Override
    public void refreshAnnualExchangeRates() {
        for (String currencyCode : CURRENCY_CODES) {
            try {
                if (currencyCode.equals("USD")) continue;
                com.github.lerocha.currency.client.ofx.HistoricalExchangeRate historicalExchangeRates = ofxClient.getHistoricalExchangeRates("USD", currencyCode, ReportingPeriod.ALL_TIME, 4, Frequency.YEARLY);
                if (historicalExchangeRates.getHistoricalPoints() != null) {
                    for (HistoricalPoint historicalPoint : historicalExchangeRates.getHistoricalPoints()) {
                        DateTime dateTime = new DateTime(historicalPoint.getPointInTime());
                        YearlyExchangeRate yearlyExchangeRate = new YearlyExchangeRate();
                        yearlyExchangeRate.setYear(dateTime.getYear());
                        yearlyExchangeRate.setCurrencyCode(historicalExchangeRates.getToCurrencyCode());
                        yearlyExchangeRate.setExchangeRate(new BigDecimal(historicalPoint.getInterbankRate()));
                        yearlyExchangeRateRepository.save(yearlyExchangeRate);
                    }
                    logger.info("status=ok; currencyCode={}", currencyCode);
                }
            } catch (Exception e) {
                logger.error("status=failed; currencyCode={}", currencyCode, e);
            }
        }
    }

    private static final String[] CURRENCY_CODES = {
            "AED",
            "ARS",
            "AUD",
            "AZN",
            "BGN",
            "BHD",
            "BND",
            "BRL",
            "CAD",
            "CHF",
            "CLP",
            "CNH",
            "CNY",
            "CZK",
            "DKK",
            "EGP",
            "EUR",
            "FJD",
            "GBP",
            "HKD",
            "HUF",
            "IDR",
            "ILS",
            "INR",
            "JPY",
            "KRW",
            "KWD",
            "LKR",
            "MAD",
            "MGA",
            "MXN",
            "MYR",
            "NOK",
            "NZD",
            "OMR",
            "PEN",
            "PGK",
            "PHP",
            "PKR",
            "PLN",
            "RUB",
            "SAR",
            "SBD",
            "SCR",
            "SEK",
            "SGD",
            "THB",
            "TOP",
            "TRY",
            "TWD",
            "TZS",
            "VEF",
            "VND",
            "VUV",
            "WST",
            "XOF",
            "XPF",
            "ZAR"
    };
}
