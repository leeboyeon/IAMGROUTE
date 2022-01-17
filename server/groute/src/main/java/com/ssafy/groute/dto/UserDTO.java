package com.ssafy.groute.dto;

import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NotNull
    private String id;

    @NotNull
    private String password;

    private String nickname;

    private String phone;

    private String gender;

    private String birth;

    private String email;

    @NotNull
    private String type;

    private String token;

    private String img;

    private String createDate;

    private String updateDate;
}
