package com.catpawdogpaw.theartimposter.match;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.catpawdogpaw.theartimposter.gameroom.model.GameRoom;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String PLAYER_COUNT_KEY_PREFIX = "gameRoom:playerCount:";
    private static final String GAME_ROOM_KEY_PREFIX = "gameRoom:";

    public void incrementPlayerCount(String gameRoomId) {
        redisTemplate.opsForValue().increment(PLAYER_COUNT_KEY_PREFIX + gameRoomId);
    }

    public void decrementPlayerCount(String gameRoomId) {
        redisTemplate.opsForValue().decrement(PLAYER_COUNT_KEY_PREFIX + gameRoomId);
    }

    public int getPlayerCount(String gameRoomId) {
        Integer count = (Integer) redisTemplate.opsForValue().get(PLAYER_COUNT_KEY_PREFIX + gameRoomId);
        return count != null ? count : 0;
    }

    public void removePlayerCount(String gameRoomId) {
        redisTemplate.delete(PLAYER_COUNT_KEY_PREFIX + gameRoomId);
    }

    public void addGameRoom(GameRoom gameRoom) {
        redisTemplate.opsForValue().set(GAME_ROOM_KEY_PREFIX + gameRoom.getGameRoomId(), gameRoom);
    }

    public void removeGameRoom(String gameRoomId) {
        redisTemplate.delete(GAME_ROOM_KEY_PREFIX + gameRoomId);
        removePlayerCount(gameRoomId);
    }

    public GameRoom getGameRoom(String gameRoomId) {
        return (GameRoom) redisTemplate.opsForValue().get(GAME_ROOM_KEY_PREFIX + gameRoomId);
    }
}
