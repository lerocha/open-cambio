package com.github.lerocha.currency.service;

import com.github.lerocha.currency.client.ofx.*;
import com.github.lerocha.currency.domain.AnnualExchangeRate;
import com.github.lerocha.currency.repository.AnnualExchangeRateRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by lerocha on 2/1/17.
 */
@Service
public class CurrencyExchangeRateServiceImpl implements CurrencyExchangeRateService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyExchangeRateService.class);

    private AnnualExchangeRateRepository annualExchangeRateRepository;

    private OfxClient ofxClient;

    @Autowired
    public CurrencyExchangeRateServiceImpl(AnnualExchangeRateRepository annualExchangeRateRepository, OfxClient ofxClient) {
        this.annualExchangeRateRepository = annualExchangeRateRepository;
        this.ofxClient = ofxClient;
    }

    @Override
    public void refreshAnnualExchangeRates() {
        for (String currencyCode : CURRENCY_CODES) {
            try {
                if (currencyCode.equals("USD")) continue;
                HistoricalExchangeRate historicalExchangeRates = ofxClient.getHistoricalExchangeRates("USD", currencyCode, ReportingPeriod.ALL_TIME, 4, Frequency.YEARLY);
                if (historicalExchangeRates.getHistoricalPoints() != null) {
                    for (HistoricalPoint historicalPoint : historicalExchangeRates.getHistoricalPoints()) {
                        DateTime dateTime = new DateTime(historicalPoint.getPointInTime());
                        AnnualExchangeRate annualExchangeRate = new AnnualExchangeRate();
                        annualExchangeRate.setYear(dateTime.getYear());
                        annualExchangeRate.setCurrencyCode(historicalExchangeRates.getToCurrencyCode());
                        annualExchangeRate.setExchangeRate(new BigDecimal(historicalPoint.getInterbankRate()));
                        annualExchangeRateRepository.save(annualExchangeRate);
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
