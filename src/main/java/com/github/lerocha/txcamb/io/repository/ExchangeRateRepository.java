/*
 * Copyright 2017 Luis Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.lerocha.txcamb.io.repository;

import com.github.lerocha.txcamb.io.domain.ExchangeRate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lerocha on 2/14/17.
 */
@RepositoryRestResource(exported = false)
public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, Long> {
    @Query(nativeQuery = true, value = "SELECT min(exchange_date) FROM exchange_rate")
    LocalDate findMinExchangeDate();

    @Query(nativeQuery = true, value = "SELECT max(exchange_date) FROM exchange_rate")
    LocalDate findMaxExchangeDate();

    List<ExchangeRate> findByExchangeDateBetweenOrderByExchangeDate(LocalDate startDate, LocalDate endDate);

    @Query(nativeQuery = true, value = "SELECT currency_code, min(exchange_date) AS start_date, max(exchange_date) AS end_date FROM exchange_rate GROUP BY currency_code")
    List<Object[]> findAvailableCurrencies();
}
