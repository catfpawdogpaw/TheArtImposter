package com.catpawdogpaw.theartimposter.config;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.catpawdogpaw.theartimposter.gameroom.GameRoomService;
import com.catpawdogpaw.theartimposter.gameroom.model.GameRoom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchHandler extends TextWebSocketHandler {

	//대기열 
    private final ConcurrentLinkedQueue<WebSocketSession> waitingUsers = new ConcurrentLinkedQueue<>();
    private final GameRoomService gameRoomService;
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("connection established: " + session.getId());
    	waitingUsers.add(session);
        checkAndMatchUsers();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    	log.info("connection closed: " + session.getId());
    	waitingUsers.remove(session);
    	
    	//게임 룸 나가는 로직
    	String gameRoomId = (String) session.getAttributes().get("gameRoomId");
        if (gameRoomId != null) {
            gameRoomService.leaveRoom(Long.parseLong(gameRoomId));
        }
    }

    private void checkAndMatchUsers() throws Exception {
        synchronized (waitingUsers) {
            while (waitingUsers.size() >= 5) {
                WebSocketSession[] matchedUsers = new WebSocketSession[5];
                for (int i = 0; i < 5; i++) {
                    matchedUsers[i] = waitingUsers.poll();
                }

                GameRoom gameRoom = gameRoomService.findOrCreateRoom(); // Find or create a game room
                for (WebSocketSession user : matchedUsers) {
                    if (user != null && user.isOpen()) {
                        gameRoomService.joinRoom(gameRoom.getGameRoomId()); // Update player count in Redis
                        user.getAttributes().put("gameRoomId", gameRoom.getGameRoomId().toString()); // Store gameRoomId in session
                        user.sendMessage(new TextMessage("Match Found: " + gameRoom.getGameRoomId()));
                    }
                }
                startGame(gameRoom, matchedUsers);
            }
        }
    }

    private void startGame(GameRoom gameRoom, WebSocketSession[] matchedUsers) {
        // 게임 시작 로직 구현
    	log.info("starting game with users : " 
    			 + java.util.Arrays.stream(matchedUsers).map(WebSocketSession::getId).toList());
    
    	synchronized(waitingUsers) {
    		 for (WebSocketSession session : waitingUsers) {
                 try {
                     session.sendMessage(new TextMessage("Game Start!"));
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }
    	}
    }
}
