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

package com.github.lerocha.txcamb.io.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by lerocha on 2/24/17.
 */
@Entity
public class Currency extends AbstractEntity implements Serializable {
    @Id
    @Column(length = 3)
    private String code;
    private String displayName;
    private LocalDate startDate;
    private LocalDate endDate;

    public Currency() {
    }

    public Currency(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public Currency(String code, String displayName, LocalDate startDate, LocalDate endDate) {
        this.code = code;
        this.displayName = displayName != null ? displayName : java.util.Currency.getInstance(code).getDisplayName();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getCurrencyCode() {
        return code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
                .append("code=").append(code)
                .append("; displayName=").append(displayName)
                .append("; startDate=").append(startDate)
                .append("; endDate=").append(endDate)
                .toString();
    }
}
