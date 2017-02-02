package com.github.lerocha.client.ofx;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "PointInTime",
        "InterbankRate",
        "InverseInterbankRate"
})
public class HistoricalPoint implements Serializable {

    @JsonProperty("PointInTime")
    private Date pointInTime;
    @JsonProperty("InterbankRate")
    private Double interbankRate;
    @JsonProperty("InverseInterbankRate")
    private Double inverseInterbankRate;
    private final static long serialVersionUID = -2906538200153197969L;

    @JsonProperty("PointInTime")
    public Date getPointInTime() {
        return pointInTime;
    }

    @JsonProperty("PointInTime")
    public void setPointInTime(Date pointInTime) {
        this.pointInTime = pointInTime;
    }

    @JsonProperty("InterbankRate")
    public Double getInterbankRate() {
        return interbankRate;
    }

    @JsonProperty("InterbankRate")
    public void setInterbankRate(Double interbankRate) {
        this.interbankRate = interbankRate;
    }

    @JsonProperty("InverseInterbankRate")
    public Double getInverseInterbankRate() {
        return inverseInterbankRate;
    }

    @JsonProperty("InverseInterbankRate")
    public void setInverseInterbankRate(Double inverseInterbankRate) {
        this.inverseInterbankRate = inverseInterbankRate;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("pointInTime=").append(pointInTime)
                .append("; interbankRate=").append(interbankRate)
                .append("; inverseInterbankRate=").append(inverseInterbankRate)
                .toString();
    }
}