package com.ssafy.groute.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanReview {
    private int id;
    private String userId;
    private int planId;
    private String title;
    private String content;
    private double rate;
    private String img;
    private String createDate;
}
