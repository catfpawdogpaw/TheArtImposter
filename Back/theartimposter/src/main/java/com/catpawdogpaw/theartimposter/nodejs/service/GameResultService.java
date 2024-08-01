package com.catpawdogpaw.theartimposter.nodejs.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.catpawdogpaw.theartimposter.common.S3.S3Service;
import com.catpawdogpaw.theartimposter.gameroom.GameRoomService;
import com.catpawdogpaw.theartimposter.gameroom.model.GameRoom;
import com.catpawdogpaw.theartimposter.player.PlayerService;
import com.catpawdogpaw.theartimposter.player.model.GameRole;
import com.catpawdogpaw.theartimposter.player.model.Player;
import com.catpawdogpaw.theartimposter.round.RoundService;
import com.catpawdogpaw.theartimposter.round.model.Round;
import com.catpawdogpaw.theartimposter.security.api.entity.UserEntity;
import com.catpawdogpaw.theartimposter.security.api.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class GameResultService {
	

    private final PlayerService playerService;
    private final RoundService roundService;
    private final GameRoomService gameRoomService;
    private final UserService userService;
    private final S3Service s3Service;
    
    
    
    
    private Round mapToRound(Map<String, Object> roundResultMap, Long roomId, DateTimeFormatter formatter) {
    	Round round = new Round();
        round.setRoomId(roomId);

        // subject 필드 처리
        if (roundResultMap.get("subject") != null) {
            Map<String, Object> subjectMap = (Map<String, Object>) roundResultMap.get("subject");
            if (subjectMap.get("subjectId") != null) {
                round.setSubjectId(Long.parseLong(subjectMap.get("subjectId").toString()));
            } else {
                throw new IllegalArgumentException("subjectId is null");
            }
        } else {
            throw new IllegalArgumentException("subject is null");
        }

        // roundNumber 필드 처리
        if (roundResultMap.get("roundNumber") != null) {
            round.setRoundNum(Integer.parseInt(roundResultMap.get("roundNumber").toString()));
        } else {
            throw new IllegalArgumentException("roundNumber is null");
        }

        // winRole 필드 처리
        if (roundResultMap.get("winRole") != null) {
            round.setWinRole(GameRole.valueOf(roundResultMap.get("winRole").toString().toUpperCase()));
        } else {
            throw new IllegalArgumentException("winRole is null");
        }

        // image 필드 처리
        if (roundResultMap.get("image") != null) {
        
            String base64Image = roundResultMap.get("image").toString();
            log.info(base64Image);
            round.setImageUrl(base64Image);
        }
        
        
        // startTime 필드 처리
        if (roundResultMap.get("startTime") != null) {
            round.setStartedAt(LocalDateTime.parse(roundResultMap.get("startTime").toString(), formatter));
         
        } else {
            throw new IllegalArgumentException("startTime is null");
        }

        // endTime 필드 처리
        if (roundResultMap.get("endTime") != null) {
            round.setFinishedAt(LocalDateTime.parse(roundResultMap.get("endTime").toString(), formatter));
        } else {
            throw new IllegalArgumentException("endTime is null");
        }

        return round;

    }
    
    public void logGameRoomResultDTO(Map<String, Object> gameRoomResultMap) {
    	System.out.println("Logging game room result: " + gameRoomResultMap);

        System.out.println("=================");
        // gameRoomResultMap.get("roundResults")는 List<Map<String, Object>> 타입으로 캐스팅
        List<Map<String, Object>> roundResults = (List<Map<String, Object>>) gameRoomResultMap.get("roundResults");
        
        // roundResults 리스트의 각 요소를 처리
        for (Map<String, Object> roundResult : roundResults) {
            // 각 roundResult는 Map<String, Object> 타입
            System.out.println("roundResult: " + roundResult);
            
            // roundResult에서 players 리스트 추출
            List<Map<String, Object>> players = (List<Map<String, Object>>) roundResult.get("players");

            for (Map<String, Object> player : players) {
                System.out.println("player: " + player);
                // player에서 필요한 필드 추출
                System.out.println("userId: " + player.get("userId"));
                System.out.println("nickName: " + player.get("nickName"));
                System.out.println("subjectId: " + roundResult.get("subject")); // subjectId는 roundResult에서 추출
            }

            System.out.println("=================");
        }
    }
   
   
	public void saveGameResult(Map<String, Object> gameRoomResultMap) {
		 Long id = Long.parseLong(gameRoomResultMap.get("gameRoomId").toString());
		    GameRoom gameRoom = gameRoomService.getGameRoom(id);

		    // DateTimeFormatter 설정
		    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
		        .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
		        .optionalStart()
		        .appendPattern("[.SSS]")
		        .optionalEnd()
		        .appendPattern("'Z'")
		        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
		        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
		        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
		        .toFormatter()
		        .withLocale(Locale.getDefault())
		        .withZone(ZoneId.of("UTC"));

		    gameRoom.setCreatedAt(LocalDateTime.parse(gameRoomResultMap.get("startTime").toString(), formatter));
		    gameRoom.setDestroyAt(LocalDateTime.parse(gameRoomResultMap.get("endTime").toString(), formatter));

		    // 게임 설정 저장 
		    Map<String, Object> gameSetting = (Map<String, Object>) gameRoomResultMap.get("gameSetting");

		    gameRoomService.updateGameRoom(gameRoom);

		    // 플레이어 저장
		    List<Map<String, Object>> players = (List<Map<String, Object>>) gameRoomResultMap.get("players");
		    players.forEach(playerMap -> playerService.savePlayerFromMap(playerMap));

		    // 라운드 결과 저장
		    List<Map<String, Object>> roundResults = (List<Map<String, Object>>) gameRoomResultMap.get("roundResults");

		    System.out.println(roundResults);

		    roundResults.forEach(roundResultMap -> {
		        System.out.println("round: " + roundResultMap);
		        // 이미지 업로드 및 URL 업데이트
		        String base64Image = roundResultMap.get("image").toString();
		        if (base64Image != null && !base64Image.isEmpty()) {
		            String imageUrl = s3Service.uploadBase64Image(base64Image);
		            roundResultMap.put("image", imageUrl);
		        }

		        // 라운드 결과 객체로 변환 및 저장
		        Round round = mapToRound(roundResultMap, gameRoom.getGameRoomId(), formatter);
		        roundService.insertRound(round);
		    });

		    // 사용자 기록 업데이트
		    roundResults.forEach(roundResultMap -> {
		        List<Map<String, Object>> playersInRound = (List<Map<String, Object>>) roundResultMap.get("players");
		        playersInRound.forEach(playerMap -> {
		            Long playerId = Long.parseLong(playerMap.get("userId").toString()); // 수정: userId로 변경
		            Player player = playerService.getPlayerById(playerId);
		            UserEntity user = userService.findById(player.getUserId());
		            user.addGameCnt();
		            String winRole = roundResultMap.get("winRole").toString();
		            if (winRole.equals(player.getGameRole().toString())) {
		                user.addVicCnt();
		            }
		            userService.update(user);
		        });
		    });	}
}
