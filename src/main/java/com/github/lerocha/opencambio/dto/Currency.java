package com.github.lerocha.opencambio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Currency extends RepresentationModel<Currency> {
    private String code;
    private String displayName;
    private LocalDate startDate;
    private LocalDate endDate;

    public Currency(com.github.lerocha.opencambio.entity.Currency currency, Link link) {
        this(currency.getCode(), currency.getDisplayName(), currency.getStartDate(), currency.getEndDate());
        this.add(link);
    }
}
