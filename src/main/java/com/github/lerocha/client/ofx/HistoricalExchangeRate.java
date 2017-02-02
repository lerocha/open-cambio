package com.github.lerocha.client.ofx;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "CurrentInterbankRate",
        "CurrentInverseInterbankRate",
        "Average",
        "HistoricalPoints"
})
public class HistoricalExchangeRate implements Serializable {

    private String fromCurrencyCode;
    private String toCurrencyCode;

    @JsonProperty("CurrentInterbankRate")
    private Double currentInterbankRate;
    @JsonProperty("CurrentInverseInterbankRate")
    private Double currentInverseInterbankRate;
    @JsonProperty("Average")
    private Double average;
    @JsonProperty("HistoricalPoints")
    private List<HistoricalPoint> historicalPoints = new ArrayList<>();
    private final static long serialVersionUID = 603875914937579710L;

    public String getFromCurrencyCode() {
        return fromCurrencyCode;
    }

    public void setFromCurrencyCode(String fromCurrencyCode) {
        this.fromCurrencyCode = fromCurrencyCode;
    }

    public String getToCurrencyCode() {
        return toCurrencyCode;
    }

    public void setToCurrencyCode(String toCurrencyCode) {
        this.toCurrencyCode = toCurrencyCode;
    }

    @JsonProperty("CurrentInterbankRate")
    public Double getCurrentInterbankRate() {
        return currentInterbankRate;
    }

    @JsonProperty("CurrentInterbankRate")
    public void setCurrentInterbankRate(Double currentInterbankRate) {
        this.currentInterbankRate = currentInterbankRate;
    }

    @JsonProperty("CurrentInverseInterbankRate")
    public Double getCurrentInverseInterbankRate() {
        return currentInverseInterbankRate;
    }

    @JsonProperty("CurrentInverseInterbankRate")
    public void setCurrentInverseInterbankRate(Double currentInverseInterbankRate) {
        this.currentInverseInterbankRate = currentInverseInterbankRate;
    }

    @JsonProperty("Average")
    public Double getAverage() {
        return average;
    }

    @JsonProperty("Average")
    public void setAverage(Double average) {
        this.average = average;
    }

    @JsonProperty("HistoricalPoints")
    public List<HistoricalPoint> getHistoricalPoints() {
        return historicalPoints;
    }

    @JsonProperty("HistoricalPoints")
    public void setHistoricalPoints(List<HistoricalPoint> historicalPoints) {
        this.historicalPoints = historicalPoints;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("fromCurrencyCode=").append(fromCurrencyCode)
                .append("; toCurrencyCode=").append(toCurrencyCode)
                .append("; currentInterbankRate=").append(currentInterbankRate)
                .append("; currentInverseInterbankRate=").append(currentInverseInterbankRate)
                .append("; average=").append(average)
                .toString();
    }
}