package com.catpawdogpaw.theartimposter.nodejs.entity.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.catpawdogpaw.theartimposter.subject.model.Subject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameRoomResultDTO {
    private List<RoundResultDTO> roundResults ;
    private List<PlayerNTSDTO> players ;
    private Long gameRoomId;
    private String gameRoomTitle;
    private int playerCount = 0;
    private int currentRound = 1;
    private int currentTurnIndex = 0;
    private List<Object> drawingData ;
    private List<Subject> subjects;
    private GameSettingDTO gameSetting;
    private LocalDateTime startTime;
    private LocalDateTime endTime ;
    private LocalDateTime roundStartTime;
}
