package com.catpawdogpaw.theartimposter.player.model;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {
	private Long id;
	private Long userId;
	private Long roundId;
	private	GameRole gameRole;
	private String turn;
	private String color;

}
