package com.catpawdogpaw.theartimposter.nodejs.entity.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class STNDTO {
	List<PlayerSTNDTO> playerList = new ArrayList<>();
	GameRoomSTNDTO gameRoom;
	GameSettingSTNDTO gameSetting;
	List<SubjectSTNDTO> subjectList = new ArrayList<>();
}
