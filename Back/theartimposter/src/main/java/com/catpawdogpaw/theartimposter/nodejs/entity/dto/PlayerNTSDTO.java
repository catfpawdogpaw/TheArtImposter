package com.catpawdogpaw.theartimposter.nodejs.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerNTSDTO {
    private Long userId;
    private String nickName;
    private String profileImage;
    private int vicCnt;
    private int gameCnt;
    private int curScore;
    private String color ;
    private String turn;
    private String gameRole;
    private String socketId;
}
