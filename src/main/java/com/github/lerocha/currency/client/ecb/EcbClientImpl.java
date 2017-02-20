package com.github.lerocha.currency.client.ecb;

import com.github.lerocha.currency.client.ecb.dto.Envelope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by lerocha on 2/20/17.
 */
@Component
public class EcbClientImpl implements EcbClient {
    private RestTemplate restTemplate;

    @Autowired
    public EcbClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final String URL = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

    @Override
    public ResponseEntity<Envelope> getCurrentExchangeRate() {
        return restTemplate.exchange(URL, HttpMethod.GET, null, Envelope.class);
    }
}
