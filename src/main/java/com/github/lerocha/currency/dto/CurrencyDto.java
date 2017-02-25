package com.github.lerocha.currency.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by lerocha on 2/24/17.
 */
public class CurrencyDto implements Serializable {
    private String currencyCode;
    private String displayName;
    private LocalDate startDate;
    private LocalDate endDate;

    public CurrencyDto() {
    }

    public CurrencyDto(String currencyCode, String displayName, LocalDate startDate, LocalDate endDate) {
        this.currencyCode = currencyCode;
        this.displayName = displayName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("currencyCode=").append(currencyCode)
                .append("; displayName=").append(displayName)
                .append("; startDate=").append(startDate)
                .append("; endDate=").append(endDate)
                .toString();
    }
}
