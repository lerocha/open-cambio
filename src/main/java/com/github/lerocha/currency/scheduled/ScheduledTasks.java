package com.github.lerocha.currency.scheduled;

import com.github.lerocha.currency.service.ExchangeRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by lerocha on 3/7/17.
 */
@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ScheduledTasks(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    /**
     * Scheduled task to refresh exchange rates.
     * ECB usually updates exchange rates around 16:00 CET (07:00 PST) on every working day, except on TARGET closing days.
     * Source: http://www.ecb.europa.eu/stats/policy_and_exchange_rates/euro_reference_exchange_rates/html/index.en.html
     */
    @Scheduled(cron = "0 30 7 * * *")
    public void refreshExchangeRatesTask() {
        try {
            exchangeRateService.refreshExchangeRates();
            log.info("refreshExchangeRatesTask; status=completed");
        } catch (Exception e) {
            log.error("refreshExchangeRatesTask; status=failed", e);
        }
    }
}
