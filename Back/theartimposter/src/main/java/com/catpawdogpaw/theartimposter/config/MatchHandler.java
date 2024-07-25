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
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("Received message: " + message.getPayload());
        super.handleTextMessage(session, message);
    }
    private void checkAndMatchUsers() throws Exception {
    	synchronized (waitingUsers) { // waitingUsers 큐에 대한 동기화 블록 시작
            while (!waitingUsers.isEmpty()) { // 대기열에 사용자가 있는 동안 반복
                WebSocketSession[] matchedUsers = new WebSocketSession[Math.min(waitingUsers.size(), 5)];
                for (int i = 0; i < matchedUsers.length; i++) {
                    matchedUsers[i] = waitingUsers.poll();
                }
                // 게임 방을 찾거나 새로 생성
                GameRoom gameRoom = gameRoomService.findOrCreateRoom();
                for (WebSocketSession user : matchedUsers) {
                    if (user != null && user.isOpen()) {
                        // Redis에서 플레이어 수 업데이트
                        gameRoomService.joinRoom(gameRoom.getGameRoomId());
                        // 세션에 gameRoomId 저장
                        user.getAttributes().put("gameRoomId", gameRoom.getGameRoomId().toString());
                        // 사용자에게 매칭 완료 메시지 전송
                        user.sendMessage(new TextMessage("Match Found: " + gameRoom.getGameRoomId()));
                        waitingUsers.remove(user);  // 사용자 제거
                    }
                }
                if (matchedUsers.length == 5) {
                    startGame(gameRoom, matchedUsers);
                } else {
                    notifyWaitingPlayers(gameRoom, matchedUsers);
                }
            }
        }
//        synchronized (waitingUsers) { // waitingUsers 큐에 대한 동기화 블록 시작
//            while (waitingUsers.size() >= 5) { // 대기열에 5명 이상의 사용자가 있는 동안 반복
//                WebSocketSession[] matchedUsers = new WebSocketSession[5];
//                for (int i = 0; i < 5; i++) {
//                    matchedUsers[i] = waitingUsers.poll();
//                }
//             // 게임 방을 찾거나 새로 생성
//                GameRoom gameRoom = gameRoomService.findOrCreateRoom(); 
//                for (WebSocketSession user : matchedUsers) {
//                    if (user != null && user.isOpen()) {
//                    	// Redis에서 플레이어 수 업데이트
//                        gameRoomService.joinRoom(gameRoom.getGameRoomId()); 
//                     // 세션에 gameRoomId 저장
//                        user.getAttributes().put("gameRoomId", gameRoom.getGameRoomId().toString()); // Store gameRoomId in session
//                     // 사용자에게 매칭 완료 메시지 전송
//                        user.sendMessage(new TextMessage("Match Found: " + gameRoom.getGameRoomId()));
//                    }
//                }
//                startGame(gameRoom, matchedUsers);
//            }
//        }
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
    
    private void notifyWaitingPlayers(GameRoom gameRoom, WebSocketSession[] matchedUsers) throws Exception {
        for (WebSocketSession session : matchedUsers) {
            if (session != null && session.isOpen()) {
                session.sendMessage(new TextMessage("Waiting for more players: " + gameRoom.getGameRoomId() 
                    + " (" + matchedUsers.length + "/5)"));
            }
        }
    }
}
