package com.github.lerocha.currency.repository;

import com.github.lerocha.currency.domain.ExchangeRate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lerocha on 2/14/17.
 */
public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, Long> {
    @Query(nativeQuery = true, value = "SELECT min(exchange_date) FROM exchange_rate")
    LocalDate findMinExchangeDate();

    @Query(nativeQuery = true, value = "SELECT max(exchange_date) FROM exchange_rate")
    LocalDate findMaxExchangeDate();

    List<ExchangeRate> findByExchangeDateOrderByCurrencyCode(LocalDate exchangeDate);

    List<ExchangeRate> findByExchangeDateBetweenOrderByExchangeDate(LocalDate startDate, LocalDate endDate);

    @Query(nativeQuery = true, value = "SELECT currency_code, min(exchange_date) AS start_date, max(exchange_date) AS end_date FROM exchange_rate GROUP BY currency_code")
    List<Object[]> findAvailableCurrencies();
}
