package com.treasury.treasuryhub.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryTotalsDto {

    private Integer transactionCategoryId;
    private String name;
    private String transactionType;
    private Double budget;
    private Double amount;

    public CategoryTotalsDto(Integer transactionCategoryId, String name, String transactionType, Double budget, Double amount) {
        this.transactionCategoryId = transactionCategoryId;
        this.name = name;
        this.transactionType = transactionType;
        this.budget = budget;
        this.amount = amount;
    }
}
