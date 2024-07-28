package com.catpawdogpaw.theartimposter.player.model;



import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RedisHash("player")
public class Player {
	private Long id;
	private Long userId;
	private Long roundId;
	private	GameRole gameRole;
	private String turn;
	private String color;

}
