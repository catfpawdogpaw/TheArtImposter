package com.catpawdogpaw.theartimposter.match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.catpawdogpaw.theartimposter.category.CategoryService;
import com.catpawdogpaw.theartimposter.config.WebSocketEventListener;
import com.catpawdogpaw.theartimposter.gameroom.GameRoomService;
import com.catpawdogpaw.theartimposter.gameroom.model.GameRoom;
import com.catpawdogpaw.theartimposter.gamesetting.GameSettingService;
import com.catpawdogpaw.theartimposter.gamesetting.model.GameSetting;
import com.catpawdogpaw.theartimposter.nodejs.entity.dto.GameRoomSTNDTO;
import com.catpawdogpaw.theartimposter.nodejs.entity.dto.GameSettingDTO;
import com.catpawdogpaw.theartimposter.nodejs.entity.dto.STNDTO;
import com.catpawdogpaw.theartimposter.nodejs.entity.dto.SubjectSTNDTO;
import com.catpawdogpaw.theartimposter.nodejs.service.NodejsService;
import com.catpawdogpaw.theartimposter.subject.SubjectService;
import com.catpawdogpaw.theartimposter.subject.model.Subject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchHandler {

	// 대기열
	private final ConcurrentLinkedQueue<WebSocketSession> waitingUsers = new ConcurrentLinkedQueue<>();
	private final GameRoomService gameRoomService;
	private final GameSettingService gameSettingService;
	private final SubjectService subjectService;
	private final CategoryService categoryService;
	private final NodejsService nodeJsService;
	private MatchHandler customWebSocketHandler;

	public WebSocketSession getSessionById(String sessionId) {
		return WebSocketEventListener.getSessionById(sessionId);
	}

	public ConcurrentMap<String, WebSocketSession> getSessionMap() {
		return WebSocketEventListener.getSessionMap();
	}

	public void addUser(String sessionId) {
        WebSocketSession session = getSessionById(sessionId);
        if (session != null) {
            log.info("Adding user with sessionId {} to waiting queue", sessionId);
            waitingUsers.add(session);
            try {
                checkAndMatchUsers();
            } catch (Exception e) {
                log.error("Error while matching users", e);
            }
        } else {
            log.warn("Session not found for sessionId: {}", sessionId);
        }
    }
	private void checkAndMatchUsers() throws Exception {
		log.info("checkAndMatchUsers");
		synchronized (waitingUsers) {
			while (waitingUsers.size() >= 5) { // 5로 변경하기
				WebSocketSession[] matchedUsers = new WebSocketSession[5];
				for (int i = 0; i < 5; i++) {
					matchedUsers[i] = waitingUsers.poll();
				}
				createAndAssignGameRoom(matchedUsers);
			}
		}
	}

	private void createAndAssignGameRoom(WebSocketSession[] matchedUsers) {
		log.info("※※※매칭 시작=============");
		// 새로운 GameRoom 생성
		GameRoom gameRoom = new GameRoom();
		gameRoom.setTitle(UUID.randomUUID().toString()); // 무작위 제목
		gameRoom.setGameSettingId(1L); // 고정된 게임 설정 ID
		
		
		Long gameRoomId = gameRoomService.createGameRoom(gameRoom);
		log.info("gameRoomId" + gameRoomId);
		
		
		STNDTO stnDTO = new STNDTO();
		GameRoomSTNDTO gameRoomStnDto = new GameRoomSTNDTO();
		gameRoomStnDto.setGameRoomId(gameRoomId.intValue());
		gameRoomStnDto.setTitle(gameRoom.getTitle());

		stnDTO.setGameRoom(gameRoomStnDto);

		GameSettingDTO gameSettingStnDto = new GameSettingDTO();

		GameSetting gameSetting = gameSettingService.getGameSettingById(gameRoom.getGameSettingId());
		gameSettingStnDto.setMaxPeople(gameSetting.getMaxPeople());
		gameSettingStnDto.setMinPeople(gameSetting.getMinPeople());
		gameSettingStnDto.setRound(gameSetting.getRound());
		gameSettingStnDto.setRoundTimeLimit(gameSetting.getRoundTimeLimit());
		gameSettingStnDto.setTurnTimeLimit(gameSetting.getTurnTimeLimit());
		gameSettingStnDto.setVersion(gameSetting.getVersion());

		
		
		stnDTO.setGameSetting(gameSettingStnDto);

		// 주제 랜덤
		List<Subject> subjectList = subjectService.getAllSubjects();
		Collections.shuffle(subjectList);

		int numberOfRounds = gameSetting.getRound();
		List<Subject> selectedSubjects = subjectList.subList(0, Math.min(numberOfRounds, subjectList.size()));

		// SubjectSTNDTO로 할당하기
		List<SubjectSTNDTO> subjectStnDtos = new ArrayList<>();
		for (Subject subject : selectedSubjects) { // 여기서 subjectList를 selectedSubjects로 변경
			SubjectSTNDTO subjectStnDto = new SubjectSTNDTO();
			subjectStnDto.setSubjectId(subject.getSubjectId().intValue());
			subjectStnDto.setCategory(categoryService.getCategoryById(subject.getCategoryId()).getCategory());
			subjectStnDto.setSubject(subject.getSubject());
			subjectStnDtos.add(subjectStnDto);
		}

		stnDTO.setSubjectList(subjectStnDtos);

		// gameRoomId를 Node.js 서버로 전송
		nodeJsService.sendToNode(stnDTO);

	    // JSON 메시지 생성
	    ObjectMapper objectMapper = new ObjectMapper();
	    Map<String, Object> message = new HashMap<>();
	    message.put("roomId", gameRoomId);
	    message.put("roomTitle", gameRoom.getTitle());

	    String jsonMessage = "";
	    try {
	        jsonMessage = objectMapper.writeValueAsString(message);
	    } catch (JsonProcessingException e) {
	        log.error("Error converting message to JSON", e);
	    }

	    //roomId, roomTitle 정보 vue에 반환 
	    for (WebSocketSession user : matchedUsers) {
	        if (user != null && user.isOpen()) {
	            user.getAttributes().put("gameRoomId", gameRoomId);
	            try {
	                user.sendMessage(new TextMessage(jsonMessage));
	                log.info("gameRoomId : " + gameRoomId);
	                log.info("gameRoomTitle : " + gameRoom.getTitle());
	                
	            } catch (Exception e) {
	                log.error("Error sending match found message", e);
	            }
	        }
	    }
//
//		for (WebSocketSession user : matchedUsers) {
//			if (user != null && user.isOpen()) {
//				user.getAttributes().put("gameRoomId", gameRoomId);
//				try {
//					user.sendMessage(new TextMessage("Match Found: " + gameRoomId));
//				} catch (Exception e) {
//					log.error("Error sending match found message", e);
//				}
//			}
//		}
	}

	public void addSession(String sessionId, WebSocketSession session) {
        WebSocketEventListener.addSession(sessionId, session);
    }

    public WebSocketSession getSession(String sessionId) {
        return WebSocketEventListener.getSessionById(sessionId);
    }

    public List<String> getCurrentSessions() {
        return WebSocketEventListener.getSessionMap().keySet().stream().collect(Collectors.toList());
    }
}
