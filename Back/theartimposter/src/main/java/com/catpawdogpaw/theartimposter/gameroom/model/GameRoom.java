package com.catpawdogpaw.theartimposter.gameroom.model;

import java.time.LocalDateTime;

import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RedisHash
public class GameRoom {
    private Long gameRoomId;
    private Long gameSettingId;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime destroyAt;
	
}
