package com.github.lerocha.currency.repository;

import com.github.lerocha.currency.domain.AnnualExchangeRate;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by lrocha3 on 2/1/17.
 */
public interface AnnualExchangeRateRepository extends CrudRepository<AnnualExchangeRate, Long> {
}
