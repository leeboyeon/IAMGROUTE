package com.ssafy.groute.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPlan {
    private int id;
    private String userId;
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private int totalDate;
    private String isPublic;
}