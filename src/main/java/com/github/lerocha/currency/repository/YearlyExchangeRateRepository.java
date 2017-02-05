package com.github.lerocha.currency.repository;

import com.github.lerocha.currency.domain.YearlyExchangeRate;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by lerocha on 2/1/17.
 */
public interface YearlyExchangeRateRepository extends CrudRepository<YearlyExchangeRate, Long> {
    Iterable<YearlyExchangeRate> findByCurrencyCode(String currencyCode);

    Iterable<YearlyExchangeRate> findByCurrencyCodeAndYear(String currencyCode, Integer year);
}
