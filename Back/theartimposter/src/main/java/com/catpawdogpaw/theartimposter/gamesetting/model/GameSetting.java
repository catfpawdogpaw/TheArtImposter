package com.catpawdogpaw.theartimposter.gamesetting.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameSetting {
	private Long gameSettingId;
	private String version;
//	private LocalDateTime turnTimeLimit;
//	 private LocalDateTime roundTimeLimit;
	private int turnTimeLimit;
	private int roundTimeLimit;
	private int minPeople;
	private int maxPeople;
	private int round; // 라운드 수 설정 추가

}
