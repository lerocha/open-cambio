package com.github.lerocha.currency;

import com.github.lerocha.currency.client.ofx.Frequency;
import com.github.lerocha.currency.client.ofx.HistoricalExchangeRate;
import com.github.lerocha.currency.client.ofx.OfxClient;
import com.github.lerocha.currency.client.ofx.ReportingPeriod;
import com.github.lerocha.currency.service.ExchangeRateService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OfxClientTests {

    @Autowired
    OfxClient ofxClient;

    @Autowired
    ExchangeRateService exchangeRateService;

    @Test
    public void getHistoricalExchangeRates() {
        // https://api.ofx.com/PublicSite.ApiService/SpotRateHistory/5year/USD/BRL?DecimalPlaces=4&ReportingInterval=yearly
        HistoricalExchangeRate historicalExchangeRate = ofxClient.getHistoricalExchangeRates("USD", "CAD", ReportingPeriod.FIVE_YEAR, 4, Frequency.YEARLY);
        Assert.assertNotNull(historicalExchangeRate);
    }

    //@Test
    public void refreshAnnualExchangeRates() {
        exchangeRateService.refreshAnnualExchangeRates();
    }
}
