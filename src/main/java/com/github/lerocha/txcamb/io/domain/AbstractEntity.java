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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.time.Clock;
import java.time.LocalDateTime;

/**
 * Created by lerocha on 2/18/17.
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity implements Serializable {
    private static final long serialVersionUID = 6639875532910837803L;

    @JsonIgnore
    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now(Clock.systemUTC());

    @JsonIgnore
    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private LocalDateTime lastModifiedDate = LocalDateTime.now(Clock.systemUTC());

    @PreUpdate
    private void preUpdate() {
        this.lastModifiedDate = LocalDateTime.now(Clock.systemUTC());
    }
}
