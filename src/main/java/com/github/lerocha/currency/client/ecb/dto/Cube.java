package com.github.lerocha.currency.client.ecb.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lerocha on 2/20/17.
 */
public class Cube {
    @JacksonXmlProperty
    private String currency;
    @JacksonXmlProperty
    private BigDecimal rate;
    @JacksonXmlProperty
    private String time;
    @JacksonXmlElementWrapper(localName = "Cube")
    private List<Cube> cubes;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Cube> getCubes() {
        return cubes;
    }

    public void setCubes(List<Cube> cube) {
        this.cubes = cube;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("currency=").append(currency)
                .append("; rate=").append(rate)
                .append("; time=").append(time)
                .toString();
    }
}
