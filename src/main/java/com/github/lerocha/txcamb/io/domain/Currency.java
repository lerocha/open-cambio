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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by lerocha on 2/24/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Currency extends AbstractEntity implements Serializable {
    @Id
    @Column(length = 3)
    private String code;
    private String displayName;
    private LocalDate startDate;
    private LocalDate endDate;

    public Currency(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
}
