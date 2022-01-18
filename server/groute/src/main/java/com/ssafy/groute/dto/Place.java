package com.ssafy.groute.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Place {
    private int id;
    private String name;
    private String type;
    private String lat;
    private String lng;
    private String zipCode;
    private String contact;
    private String address;
    private String description;
    private int themeId;
    private int areaId;
    private String img;
    private String userId;

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", contact='" + contact + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", themeId=" + themeId +
                ", areaId=" + areaId +
                ", img='" + img + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
