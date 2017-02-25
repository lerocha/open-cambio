package com.github.lerocha.currency.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by lerocha on 2/24/17.
 */
@Entity
public class Currency implements Serializable {
    @Id
    @Column(length = 3)
    private String currencyCode;
    private String displayName;
    private LocalDate startDate;
    private LocalDate endDate;

    public Currency() {
    }

    public Currency(String currencyCode, String displayName) {
        this.currencyCode = currencyCode;
        this.displayName = displayName;
    }

    public Currency(String currencyCode, String displayName, LocalDate startDate, LocalDate endDate) {
        this.currencyCode = currencyCode;
        this.displayName = displayName != null ? displayName : java.util.Currency.getInstance(currencyCode).getDisplayName();
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
