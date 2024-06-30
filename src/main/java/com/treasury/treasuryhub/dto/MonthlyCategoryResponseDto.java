package com.treasury.treasuryhub.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MonthlyCategoryResponseDto {

    private List<String> months;
    private List<Double> monthlyExpenses;

    public MonthlyCategoryResponseDto(List<String> months, List<Double> monthlyExpenses) {
        this.months = months;
        this.monthlyExpenses = monthlyExpenses;
    }
}
