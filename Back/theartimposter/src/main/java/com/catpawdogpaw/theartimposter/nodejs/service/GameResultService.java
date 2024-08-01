package com.catpawdogpaw.theartimposter.nodejs.service;

import org.springframework.stereotype.Service;

import com.catpawdogpaw.theartimposter.common.S3.S3Service;
import com.catpawdogpaw.theartimposter.gameroom.GameRoomService;
import com.catpawdogpaw.theartimposter.gameroom.model.GameRoom;
import com.catpawdogpaw.theartimposter.nodejs.entity.dto.GameRoomResultDTO;
import com.catpawdogpaw.theartimposter.nodejs.entity.dto.GameSettingDTO;
import com.catpawdogpaw.theartimposter.nodejs.entity.dto.PlayerSTNDTO;
import com.catpawdogpaw.theartimposter.nodejs.entity.dto.RoundResultDTO;
import com.catpawdogpaw.theartimposter.player.PlayerService;
import com.catpawdogpaw.theartimposter.player.model.Player;
import com.catpawdogpaw.theartimposter.round.RoundService;
import com.catpawdogpaw.theartimposter.security.api.entity.UserEntity;
import com.catpawdogpaw.theartimposter.security.api.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GameResultService {
	

    private final PlayerService playerService;
    private final RoundService roundService;
    private final GameRoomService gameRoomService;
    private final UserService userService;
    private final S3Service s3Service;
    
    
	public void saveGameResult(GameRoomResultDTO gameRoomResultDTO) {
	    // Save GameRoomResult
		Long id = gameRoomResultDTO.getGameRoomId();
		GameRoom gameRoom = gameRoomService.getGameRoom(id);
		gameRoom.setCreatedAt(gameRoomResultDTO.getStartTime());
		gameRoom.setDestroyAt(gameRoomResultDTO.getEndTime());
		GameSettingDTO gameSetting = gameRoomResultDTO.getGameSetting();
        gameRoomService.updateGameRoom(gameRoom);

        // Save players
        gameRoomResultDTO.getPlayers().forEach(playerNTSDTO -> playerService.savePlayerFromDTO(playerNTSDTO));

        // Save round results
        gameRoomResultDTO.getRoundResults().forEach(roundResult -> {
            roundService.saveRoundResult(roundResult);
//            roundResult.getPlayers().forEach(player -> roundService.saveRoundPlayer(roundResult.getRoundNumber(), player));
        });
		
        
        //전적 계산
       for(RoundResultDTO roundResultDto : gameRoomResultDTO.getRoundResults()) {
    	   for(PlayerSTNDTO playerDto : roundResultDto.getPlayers()) {
    		   // 플레이어 정보 불러오기
    		   Player player = playerService.getPlayerById( playerDto.getPlayerId().longValue());
    		   // 플레이어의 user_id 로 사용자 조회
    		   UserEntity user =userService.findById( player.getUserId());
    		   user.addGameCnt();
    		   // 이긴 경우 
    		   if( roundResultDto.getWinRole().toString().equals(player.getGameRole())){ // Role의 문자열 비교
    			   user.addVicCnt();
    		   }
    		   userService.update(user); // 수정 정보 반영
    		   
    	   }
       }
       
       
       // 이미지 저장
       
      for(RoundResultDTO roundResultDto :gameRoomResultDTO.getRoundResults()) {
    	  String base64Image = roundResultDto.getImage();
          if (base64Image != null && !base64Image.isEmpty()) {
              String imageUrl = s3Service.uploadBase64Image(base64Image);
              roundResultDto.setImage(imageUrl); // Store the S3 URL back in the DTO
          }
      }
	}
}
