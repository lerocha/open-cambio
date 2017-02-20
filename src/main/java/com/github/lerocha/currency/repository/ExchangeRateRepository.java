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
    @Query(nativeQuery = true, value = "SELECT max(exchange_date) FROM exchange_rate")
    LocalDate findMaxExchangeDate();

    List<ExchangeRate> findByExchangeDateOrderByCurrencyCode(LocalDate exchangeDate);
}
