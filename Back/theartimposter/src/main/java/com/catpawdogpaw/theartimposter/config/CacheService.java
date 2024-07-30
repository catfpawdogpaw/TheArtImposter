package com.catpawdogpaw.theartimposter.config;


import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.catpawdogpaw.theartimposter.gameroom.model.GameRoom;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, String> customStringRedisTemplate;

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

    /*
    * Refresh 토큰 저장, 조회, 삭제, 업데이트
    * */
    public void saveUserData(Long userId, String refreshToken, String nickname, String image, Long vicCnt, Long gameCnt, Long expiry) {

        Map<String, Object> userData = Map.of(
                "refreshToken", refreshToken,
                "userId", userId.toString(),
                "nickname", nickname,
                "profileImage", image,
                "vicCnt", vicCnt.toString(),
                "gameCnt", gameCnt.toString()
        );

//        redisTemplate.opsForValue().set(refreshToken, userData, expiry, TimeUnit.MILLISECONDS);
        customStringRedisTemplate.opsForHash().putAll("userData:"+userId, userData);
        customStringRedisTemplate.expire("userData:" + userId, expiry, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String userId) {
        return customStringRedisTemplate.opsForValue().get("refreshToken:" + userId);
    }

    public void saveRefreshToken(Long userId, String refreshToken, Long duration) {
        customStringRedisTemplate.opsForValue().set("refreshToken:" + userId, refreshToken, duration, TimeUnit.MILLISECONDS);
    }

    public void deleteRefreshToken(String userId) {
        redisTemplate.delete("refreshToken:" + userId);
    }

    public void deleteUserData(String userId) {
        redisTemplate.delete("userData:" + userId);
    }

}