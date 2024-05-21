package com.treasury.treasuryhub.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDto {

    private int transactionCategoryId;
    private String type;
    private Double amount;
    private int sourceAccountId;
    private int destinationAccountId;
    private String details;
    private String date;
}
