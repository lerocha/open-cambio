package com.github.lerocha.opencambio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagination {
    private int offset;
    private int total;
}
