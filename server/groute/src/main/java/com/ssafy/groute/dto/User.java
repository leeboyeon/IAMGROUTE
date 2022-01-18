package com.ssafy.groute.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;

    private String password;

    private String nickname;

    private String phone;

    private String gender;

    private String birth;

    private String email;

    private String type;

    private String token;

    private String img;

    private String createDate;

    private String updateDate;
}
