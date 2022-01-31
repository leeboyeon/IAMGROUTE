package com.ssafy.groute.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDetail {
    private int id;
    private String title;
    private String content;
    private String img;
    private String createDate;
    private String updateDate;
    private int heartCnt;
    private int hitCnt;
    private int boardId;
    private String userId;
    private int placeId;
}
