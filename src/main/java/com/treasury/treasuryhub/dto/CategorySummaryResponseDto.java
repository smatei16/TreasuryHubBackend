package com.treasury.treasuryhub.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategorySummaryResponseDto {

    private List<String> categoryNames;
    private List<Double> totalPerCategory;
    private List<Double> categoryBudgets;

    public CategorySummaryResponseDto(List<String> categoryNames, List<Double> totalPerCategory, List<Double> categoryBudgets) {
        this.categoryNames = categoryNames;
        this.totalPerCategory = totalPerCategory;
        this.categoryBudgets = categoryBudgets;
    }
}
