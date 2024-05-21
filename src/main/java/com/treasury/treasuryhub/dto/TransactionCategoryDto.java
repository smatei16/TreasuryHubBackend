package com.treasury.treasuryhub.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionCategoryDto {

    private String name;
    private String transactionType;
    //TH-32 temporarily adding budget here to ease up the solution a bit - might upgrade later with versioning
    private double budget;
}
