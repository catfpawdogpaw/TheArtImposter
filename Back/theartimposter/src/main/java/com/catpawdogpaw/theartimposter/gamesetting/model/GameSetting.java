package com.catpawdogpaw.theartimposter.gamesetting.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameSetting {
	private Long game_setting_id;
	private String version;
	private LocalDateTime turn_time_limit;
	private LocalDateTime round_time_limit;
	private int min_people;
	private int max_people;
	private int round; // 라운드 수 설정 추가

}
