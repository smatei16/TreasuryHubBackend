package com.treasury.treasuryhub.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDto {

    private int transactionCategoryId;
    private String type;
    private Double amount;
    private int sourceAccountId;
    private int destinationAccountId;
    private String details;
}
