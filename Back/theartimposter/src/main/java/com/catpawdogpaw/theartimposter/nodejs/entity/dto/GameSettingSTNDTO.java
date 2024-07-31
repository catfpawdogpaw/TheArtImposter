package com.catpawdogpaw.theartimposter.nodejs.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameSettingSTNDTO {

	String version;
	Integer turnTimeLimit;
	Integer roundTimeLimit;
	Integer minPeople;
	Integer maxPeople;
	Integer round;
	
	
}
