package com.catpawdogpaw.theartimposter.match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.catpawdogpaw.theartimposter.gameroom.GameRoomService;
import com.catpawdogpaw.theartimposter.gameroom.model.GameRoom;
import com.catpawdogpaw.theartimposter.gamesetting.GameSettingService;
import com.catpawdogpaw.theartimposter.gamesetting.model.GameSetting;
import com.catpawdogpaw.theartimposter.nodejs.entity.dto.GameRoomSTNDTO;
import com.catpawdogpaw.theartimposter.nodejs.entity.dto.GameSettingSTNDTO;
import com.catpawdogpaw.theartimposter.nodejs.entity.dto.STNDTO;
import com.catpawdogpaw.theartimposter.nodejs.entity.dto.SubjectSTNDTO;
import com.catpawdogpaw.theartimposter.nodejs.service.NodejsService;
import com.catpawdogpaw.theartimposter.subject.SubjectService;
import com.catpawdogpaw.theartimposter.subject.model.Subject;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchHandler  {

    // 대기열 
    private final ConcurrentLinkedQueue<WebSocketSession> waitingUsers = new ConcurrentLinkedQueue<>();
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    private final GameRoomService gameRoomService;
    private final GameSettingService gameSettingService;
    private final SubjectService subjectService;
    private final NodejsService nodeJsService;
	private MatchHandler customWebSocketHandler;
    
    public WebSocketSession getSessionById(String sessionId) {
        return sessionMap.get(sessionId);
    }
    public Map<String, WebSocketSession> getSessionMap() {
        return sessionMap;
    }
  
    public void addUser(String sessionId) {
    	log.info("add user " );
        WebSocketSession session = sessionMap.get(sessionId);
        log.info(sessionId);
        if (session != null) {
        	log.info("add waitinguser");
            waitingUsers.add(session);
            try {
                checkAndMatchUsers();
            } catch (Exception e) {
                log.error("Error while matching users", e);
            }
        }else {
            log.warn("Session not found for sessionId: " + sessionId);
        }
    }


    private void checkAndMatchUsers() throws Exception {
    	log.info("checkAndMatchUsers" );
        synchronized (waitingUsers) {
            while (waitingUsers.size() >= 5) {
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
        gameRoom.setTitle(UUID.randomUUID().toString());  // 무작위 제목
        gameRoom.setGameSettingId(1L); // 고정된 게임 설정 ID

        Long gameRoomId = gameRoomService.createGameRoom(gameRoom);
        
        STNDTO stnDTO = new STNDTO();
        GameRoomSTNDTO gameRoomStnDto = new GameRoomSTNDTO();
        gameRoomStnDto.setGameRoomId(gameRoomId.intValue());
        gameRoomStnDto.setTitle(gameRoom.getTitle());
        
        stnDTO.setGameRoom(gameRoomStnDto);
        
        GameSettingSTNDTO gameSettingStnDto = new GameSettingSTNDTO();
        
        GameSetting gameSetting = gameSettingService.getGameSettingById(gameRoom.getGameSettingId());
        gameSettingStnDto.setMaxPeople(gameSetting.getMaxPeople());
        gameSettingStnDto.setMinPeople(gameSetting.getMaxPeople());
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
        for (Subject subject : selectedSubjects) {  // 여기서 subjectList를 selectedSubjects로 변경
            SubjectSTNDTO subjectStnDto = new SubjectSTNDTO();
            subjectStnDto.setSubjectId(subject.getSubjectId().intValue());
            subjectStnDto.setCategory(subject.getCategory().toString());
            subjectStnDto.setSubject(subject.getSubject());
            subjectStnDtos.add(subjectStnDto);
        }

        stnDTO.setSubjectList(subjectStnDtos);
        
        // gameRoomId를 Node.js 서버로 전송
        nodeJsService.sendToNode(stnDTO);

        for (WebSocketSession user : matchedUsers) {
            if (user != null && user.isOpen()) {
                user.getAttributes().put("gameRoomId", gameRoomId.toString());
                try {
                    user.sendMessage(new TextMessage("Match Found: " + gameRoomId));
                } catch (Exception e) {
                    log.error("Error sending match found message", e);
                }
            }
        }
    }


    public WebSocketSession getSession(String sessionId) {
        return customWebSocketHandler.getSession(sessionId);
    }
    
    
    // 현재 접속자 조회 메서드 추가
    public List<String> getCurrentSessions() {
        return sessionMap.keySet().stream().collect(Collectors.toList());
    }
}
