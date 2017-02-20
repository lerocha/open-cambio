package com.github.lerocha.currency;

import com.github.lerocha.currency.client.ecb.EcbClient;
import com.github.lerocha.currency.client.ecb.dto.CurrencyExchangeRate;
import com.github.lerocha.currency.client.ecb.dto.DailyExchangeRate;
import com.github.lerocha.currency.client.ecb.dto.ExchangeRatesResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by lerocha on 2/20/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EcbClientTests {

    @Autowired
    EcbClient ecbClient;

    @Test
    public void getCurrentExchangeRate() {
        ResponseEntity<ExchangeRatesResponse> response = ecbClient.getCurrentExchangeRates();
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        ExchangeRatesResponse exchangeRatesResponse = response.getBody();
        Assert.assertNotNull(exchangeRatesResponse);
        Assert.assertEquals(exchangeRatesResponse.getSubject(), "Reference rates");
        Assert.assertNotNull(exchangeRatesResponse.getSender());
        Assert.assertEquals(exchangeRatesResponse.getSender().getName(), "European Central Bank");
        Assert.assertNotNull(exchangeRatesResponse.getDailyExchangeRates());
        Assert.assertTrue(exchangeRatesResponse.getDailyExchangeRates().size() > 0);
        DailyExchangeRate dailyExchangeRate = exchangeRatesResponse.getDailyExchangeRates().get(0);
        Assert.assertNotNull(dailyExchangeRate);
        Assert.assertNotNull(dailyExchangeRate.getDate());
        List<CurrencyExchangeRate> currencyExchangeRates = dailyExchangeRate.getCurrencyExchangeRates();
        Assert.assertNotNull(currencyExchangeRates);
        Assert.assertTrue(currencyExchangeRates.size() > 0);
        Assert.assertNotNull(currencyExchangeRates.get(0).getCurrency());
        Assert.assertNotNull(currencyExchangeRates.get(0).getRate());
    }

    @Test
    public void getLast90DaysExchangeRates() {
        ResponseEntity<ExchangeRatesResponse> response = ecbClient.getLast90DaysExchangeRates();
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        ExchangeRatesResponse exchangeRatesResponse = response.getBody();
        Assert.assertNotNull(exchangeRatesResponse);
        Assert.assertEquals(exchangeRatesResponse.getSubject(), "Reference rates");
        Assert.assertNotNull(exchangeRatesResponse.getSender());
        Assert.assertEquals(exchangeRatesResponse.getSender().getName(), "European Central Bank");
        Assert.assertNotNull(exchangeRatesResponse.getDailyExchangeRates());
        Assert.assertTrue(exchangeRatesResponse.getDailyExchangeRates().size() > 0);
        DailyExchangeRate dailyExchangeRate = exchangeRatesResponse.getDailyExchangeRates().get(0);
        Assert.assertNotNull(dailyExchangeRate);
        Assert.assertNotNull(dailyExchangeRate.getDate());
        List<CurrencyExchangeRate> currencyExchangeRates = dailyExchangeRate.getCurrencyExchangeRates();
        Assert.assertNotNull(currencyExchangeRates);
        Assert.assertTrue(currencyExchangeRates.size() > 0);
        Assert.assertNotNull(currencyExchangeRates.get(0).getCurrency());
        Assert.assertNotNull(currencyExchangeRates.get(0).getRate());
    }
}
