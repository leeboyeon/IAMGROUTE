package com.ssafy.groute.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private int id;
    private int spentMoney;
    private String description;
    private String category;
    private String cateImg;
    private int userPlanId;
    private String type;
    private int day;
}
