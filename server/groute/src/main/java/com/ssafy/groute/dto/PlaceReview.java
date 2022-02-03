package com.ssafy.groute.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceReview {
    private int id;
    private String userId;
    private int placeId;
    private String title;
    private String content;
    private double rate;
    private String img;
    private String createDate;
}
