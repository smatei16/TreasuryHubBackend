package com.treasury.treasuryhub.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDto {

    private Integer transactionCategoryId;
    private Double amount;
    private Integer sourceAccountId;
    private Integer destinationAccountId;
    private String merchant;
    private String details;
    private String date;
    private String url;
}
