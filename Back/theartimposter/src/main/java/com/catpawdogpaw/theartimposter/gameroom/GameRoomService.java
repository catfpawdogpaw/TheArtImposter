package com.catpawdogpaw.theartimposter.gameroom;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.catpawdogpaw.theartimposter.gameroom.mapper.GameRoomMapper;
import com.catpawdogpaw.theartimposter.gameroom.model.GameRoom;
import com.catpawdogpaw.theartimposter.match.CacheService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameRoomService {
	private final GameRoomMapper gameRoomMapper;
	private final CacheService cacheService;

	public Long createGameRoom(GameRoom gameRoom) {
        gameRoomMapper.createGameRoom(gameRoom);
        return gameRoom.getGameRoomId();
    }
	
	public void deleteGameRoom(Long gameRoomId) {
		gameRoomMapper.deleteGameRoom(gameRoomId);
	}
	
	
	public GameRoom findOrCreateRoom() {
		// 현재 존재하는 모든 게임 방 목록 가져옴 
		List<GameRoom> rooms = gameRoomMapper.findAll();
		
		// Redis에서 저장된 게임 방 목록에서  
		// 접속 인원 5명 미만인 방 찾음 (현재 접속할 수 있는 방)
		GameRoom gameRoom = rooms.stream().filter(room -> room.getDestroyAt().isAfter(LocalDateTime.now())
				&& cacheService.getPlayerCount(room.getGameRoomId().toString()) < 5).findFirst().orElse(null);

		// 접속 가능한 방이 없으면 새로운 방 생성
		if (gameRoom == null) {
			gameRoom = new GameRoom();
			gameRoom.setGameSettingId(1L); // Example gameSettingId
			gameRoom.setCreatedAt(LocalDateTime.now());
			gameRoom.setDestroyAt(LocalDateTime.now().plusHours(1)); // Room will be destroyed in 1 hour

			gameRoomMapper.createGameRoom(gameRoom);
			cacheService.addGameRoom(gameRoom);
		}
		return gameRoom;
	}

	public void joinRoom(Long gameRoomId) {
		cacheService.incrementPlayerCount(gameRoomId.toString());
	}

	public void leaveRoom(Long gameRoomId) {
		cacheService.decrementPlayerCount(gameRoomId.toString());
		int playerCount = cacheService.getPlayerCount(gameRoomId.toString());

		if (playerCount == 0) {
			gameRoomMapper.deleteGameRoom(gameRoomId);
			cacheService.removeGameRoom(gameRoomId.toString());
		}
	}
}
