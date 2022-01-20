package com.ssafy.groute.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private int id;
    private String content;
    private int level;
    private int groupNum;
    private int order;
    private int boardDetailId;
    private String userId;
}
