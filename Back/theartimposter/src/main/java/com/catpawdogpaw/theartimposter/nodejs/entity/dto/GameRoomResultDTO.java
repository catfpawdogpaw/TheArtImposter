package com.catpawdogpaw.theartimposter.nodejs.entity.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.catpawdogpaw.theartimposter.subject.model.Subject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameRoomResultDTO {
    private List<RoundResultDTO> roundResults = new ArrayList<>();
    private List<PlayerNTSDTO> players = new ArrayList<>();
    private String gameRoomId;
    private String gameRoomTitle;
    private int playerCount = 0;
    private int currentRound = 1;
    private int currentTurnIndex = 0;
    private List<Object> drawingData = new ArrayList<>();
    private List<Subject> subjects;
    private GameSettingDTO gameSetting;
    private Date startTime = new Date();
    private Date endTime = null;
    private Date roundStartTime = new Date();
}
