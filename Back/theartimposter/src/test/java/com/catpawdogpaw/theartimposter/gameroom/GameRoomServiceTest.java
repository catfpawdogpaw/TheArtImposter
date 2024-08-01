package com.catpawdogpaw.theartimposter.gameroom;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.catpawdogpaw.theartimposter.gameroom.model.GameRoom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class GameRoomServiceTest {
	
	@Autowired
	private GameRoomService gameRoomService;
	
	@Test
	public void testCreateGameRoom() {
        GameRoom gameRoom = new GameRoom();
        gameRoom.setGameSettingId(1L);
        gameRoom.setTitle("Test Room");
        gameRoom.setCreatedAt(LocalDateTime.now());
        gameRoom.setDestroyAt(LocalDateTime.now().plusHours(1));

        Long gameRoomId = gameRoomService.createGameRoom(gameRoom);
        log.info("test create game room: " + gameRoom.getGameRoomId() + " " + gameRoom.getTitle());
        assertThat(gameRoomId).isNotNull();
    }
	@Test
    public void testDeleteGameRoom() {
        GameRoom gameRoom = new GameRoom();
        gameRoom.setGameSettingId(1L);
        gameRoom.setTitle("Test Room for Deletion");
        gameRoom.setCreatedAt(LocalDateTime.now());
        gameRoom.setDestroyAt(LocalDateTime.now().plusHours(1));

        Long gameRoomId = gameRoomService.createGameRoom(gameRoom);
        assertThat(gameRoomId).isNotNull();

        gameRoomService.deleteGameRoom(gameRoomId);
        GameRoom deletedGameRoom = gameRoomService.getGameRoom(gameRoomId);
        assertThat(deletedGameRoom).isNull();
        log.info("test delete game room: " + gameRoomId);
    }

    @Test
    public void testGetGameRoom() {
        GameRoom gameRoom = new GameRoom();
        gameRoom.setGameSettingId(1L);
        gameRoom.setTitle("Test Room for Retrieval");
        gameRoom.setCreatedAt(LocalDateTime.now());
        gameRoom.setDestroyAt(LocalDateTime.now().plusHours(1));

        Long gameRoomId = gameRoomService.createGameRoom(gameRoom);
        assertThat(gameRoomId).isNotNull();

        GameRoom retrievedGameRoom = gameRoomService.getGameRoom(gameRoomId);
        assertThat(retrievedGameRoom).isNotNull();
        assertThat(retrievedGameRoom.getGameRoomId()).isEqualTo(gameRoomId);
        log.info("test get game room: " + retrievedGameRoom.getGameRoomId() + " " + retrievedGameRoom.getTitle());
    }

    @Test
    public void testFindAllGameRooms() {
        GameRoom gameRoom1 = new GameRoom();
        gameRoom1.setGameSettingId(1L);
        gameRoom1.setTitle("Test Room 1");
        gameRoom1.setCreatedAt(LocalDateTime.now());
        gameRoom1.setDestroyAt(LocalDateTime.now().plusHours(1));

        GameRoom gameRoom2 = new GameRoom();
        gameRoom2.setGameSettingId(1L);
        gameRoom2.setTitle("Test Room 2");
        gameRoom2.setCreatedAt(LocalDateTime.now());
        gameRoom2.setDestroyAt(LocalDateTime.now().plusHours(1));

        gameRoomService.createGameRoom(gameRoom1);
        gameRoomService.createGameRoom(gameRoom2);

        List<GameRoom> allGameRooms = gameRoomService.findAllGameRoom();
        assertThat(allGameRooms).isNotEmpty();
        for (GameRoom gameRoom : allGameRooms) {
            assertThat(gameRoom.getGameRoomId()).isNotNull();
            log.info("test find all game rooms: " + gameRoom.getGameRoomId() + " " + gameRoom.getTitle());
        }
    }

    @Test
    public void testUpdateGameRoom() {
        GameRoom gameRoom = new GameRoom();
        gameRoom.setGameSettingId(1L);
        gameRoom.setTitle("Test Room for Update");
        gameRoom.setCreatedAt(LocalDateTime.now());
        gameRoom.setDestroyAt(LocalDateTime.now().plusHours(1));

        Long gameRoomId = gameRoomService.createGameRoom(gameRoom);
        assertThat(gameRoomId).isNotNull();

        gameRoom.setTitle("Updated Test Room");
        gameRoomService.updateGameRoom(gameRoom);

        GameRoom updatedGameRoom = gameRoomService.getGameRoom(gameRoomId);
        assertThat(updatedGameRoom).isNotNull();
        assertThat(updatedGameRoom.getTitle()).isEqualTo("Updated Test Room");
        log.info("test update game room: " + updatedGameRoom.getGameRoomId() + " " + updatedGameRoom.getTitle());
    }
	
	
}
