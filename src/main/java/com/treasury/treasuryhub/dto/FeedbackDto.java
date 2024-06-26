package com.treasury.treasuryhub.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class FeedbackDto {

    private int rating;
    private String frequency;
//    private List<String> favouriteFunctionalities;
    private String comment;
}
