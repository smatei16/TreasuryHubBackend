package com.treasury.treasuryhub.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class DetailedTransactionResponseDto {

    private Integer id;
    private Integer userId;
    private Integer transactionCategoryId;
    private String name;
    private String type;
    private Double amount;
    private Integer sourceAccountId;
    private String sourceAccountBankName;
    private Integer destinationAccountId;
    private String destinationAccountBankName;
    private String merchant;
    private String details;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Timestamp date;
    private String url;
    private String sourceAccountCurrency;
    private String destinationAccountCurrency;

    public DetailedTransactionResponseDto(Integer id, Integer userId, Integer transactionCategoryId, String name, String type, Double amount,
                                          Integer sourceAccountId, String sourceAccountBankName, Integer destinationAccountId,
                                          String destinationAccountBankName, String merchant, String details, Timestamp date, String url,
                                          String sourceAccountCurrency, String destinationAccountCurrency) {
        this.id = id;
        this.userId = userId;
        this.transactionCategoryId =  transactionCategoryId;
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.sourceAccountId = sourceAccountId;
        this.sourceAccountBankName = sourceAccountBankName;
        this.destinationAccountId = destinationAccountId;
        this.destinationAccountBankName = destinationAccountBankName;
        this.merchant = merchant;
        this.details = details;
        this.date = date;
        this.url = url;
        this.sourceAccountCurrency = sourceAccountCurrency;
        this.destinationAccountCurrency = destinationAccountCurrency;
    }
}
