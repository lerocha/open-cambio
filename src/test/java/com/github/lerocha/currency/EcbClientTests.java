package com.github.lerocha.currency;

import com.github.lerocha.currency.client.ecb.EcbClient;
import com.github.lerocha.currency.client.ecb.dto.Cube;
import com.github.lerocha.currency.client.ecb.dto.Envelope;
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
        ResponseEntity<Envelope> response = ecbClient.getCurrentExchangeRates();
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Envelope envelope = response.getBody();
        Assert.assertNotNull(envelope);
        Assert.assertEquals(envelope.getSubject(), "Reference rates");
        Assert.assertNotNull(envelope.getSender());
        Assert.assertEquals(envelope.getSender().getName(), "European Central Bank");
        Assert.assertNotNull(envelope.getCube());
        List<Cube> cubes = envelope.getCube().getCubes();
        Assert.assertNotNull(cubes);
        Assert.assertTrue(cubes.size() > 0);
        Assert.assertNotNull(cubes.get(0).getCurrency());
        Assert.assertNotNull(cubes.get(0).getRate());
    }
}
