package com.treasury.treasuryhub.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserStockDto {

    private String symbol;
    private Double quantity;
    private Double purchasePrice;
    private LocalDateTime purchaseDate;

}
