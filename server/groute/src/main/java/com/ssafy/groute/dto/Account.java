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
    private int routesId;
}
