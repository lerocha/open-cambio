package com.github.lerocha.client.ofx;

/**
 * Created by lrocha3 on 2/1/17.
 */
public enum ReportingPeriod {
    YEAR("year"),
    FIVE_YEAR("5year"),
    ALL_TIME("allTime");

    ReportingPeriod(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
