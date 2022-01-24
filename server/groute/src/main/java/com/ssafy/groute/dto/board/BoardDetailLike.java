package com.ssafy.groute.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDetailLike {
    private int id;
    private int boardDetailId;
    private String userId;
}
