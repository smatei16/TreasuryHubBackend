package com.treasury.treasuryhub.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MonthlySummaryResponseDto {
    private List<String> months;
    private List<Double> totalExpenses;
    private List<Double> totalIncomes;

    public MonthlySummaryResponseDto(List<String> months, List<Double> totalExpenses, List<Double> totalIncomes) {
        this.months = months;
        this.totalExpenses = totalExpenses;
        this.totalIncomes = totalIncomes;
    }
}
