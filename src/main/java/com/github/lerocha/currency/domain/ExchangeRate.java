package com.github.lerocha.currency.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by lerocha on 2/14/17.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UK_ExchangeRate_currency_code_exchange_date", columnNames = {"currencyCode", "exchangeDate"})
})
public class ExchangeRate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @CreatedDate
//    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private LocalDateTime createdDate;

    @Column(nullable = false)
    @LastModifiedDate
//    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private LocalDateTime lastModifiedDate;

    @Column(length = 4, nullable = false)
    private String currencyCode;

    @Column(nullable = false)
    private LocalDate exchangeDate;

    @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal exchangeRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public LocalDate getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(LocalDate exchangeDate) {
        this.exchangeDate = exchangeDate;
    }
}
