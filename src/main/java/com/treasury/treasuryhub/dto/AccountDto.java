package com.treasury.treasuryhub.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class AccountDto {

    private String bankName;
    private String accountType;
    private String accountNumber;
    private Double balance;
}
