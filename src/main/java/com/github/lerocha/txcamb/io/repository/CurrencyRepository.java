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

import com.github.lerocha.txcamb.io.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by lerocha on 2/25/17.
 */
@RepositoryRestResource(exported = false)
public interface CurrencyRepository extends JpaRepository<Currency, String> {
    Currency findByCode(String code);

    @RestResource(exported = false)
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE currency c CROSS JOIN " +
            "(SELECT currency_code, min(exchange_date) AS start_date, max(exchange_date) AS end_date FROM exchange_rate GROUP BY currency_code) cdate\n" +
            "SET c.start_date = cdate.start_date, c.end_date = cdate.end_date\n" +
            "WHERE c.code = cdate.currency_code")
    int updateCurrencyStartAndEndDates();
}
