package com.ssafy.groute.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Route {
    private int id;
    private String name;
    private int day;
    private String memo;
    private String isCustom;
}
