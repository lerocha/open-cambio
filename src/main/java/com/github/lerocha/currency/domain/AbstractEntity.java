package com.github.lerocha.currency.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Clock;
import java.time.LocalDateTime;

/**
 * Created by lerocha on 2/18/17.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity implements Serializable {

    @JsonIgnore
    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now(Clock.systemUTC());

    @JsonIgnore
    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", nullable = false)
    private LocalDateTime lastModifiedDate = LocalDateTime.now(Clock.systemUTC());

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
}
