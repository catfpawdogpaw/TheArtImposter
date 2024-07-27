package com.catpawdogpaw.theartimposter.gameroom;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catpawdogpaw.theartimposter.gameroom.model.GameRoom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/gameroom")
public class GameRoomController {

	private final GameRoomService gameRoomService;

	@PostMapping("/create")
    public ResponseEntity<Long> createGameRoom(@RequestBody GameRoom gameRoom) {
        Long gameRoomId = gameRoomService.createGameRoom(gameRoom);
        return ResponseEntity.ok(gameRoomId);
    }

    @DeleteMapping("/delete/{gameRoomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable("gameRoomId") Long id) {
    	log.info("게임룸 생성");
        gameRoomService.deleteGameRoom(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/findOrCreate")
    public ResponseEntity<GameRoom> findOrCreateRoom() {
        GameRoom gameRoom = gameRoomService.findOrCreateRoom();
        return ResponseEntity.ok(gameRoom);
    }

    @PostMapping("/join/{gameRoomId}")
    public ResponseEntity<Void> joinRoom(@PathVariable Long gameRoomId) {
        gameRoomService.joinRoom(gameRoomId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/leave/{gameRoomId}")
    public ResponseEntity<Void> leaveRoom(@PathVariable Long gameRoomId) {
        gameRoomService.leaveRoom(gameRoomId);
        return ResponseEntity.ok().build();
    }
}
