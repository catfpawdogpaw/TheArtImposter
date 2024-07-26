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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        gameRoomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
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
