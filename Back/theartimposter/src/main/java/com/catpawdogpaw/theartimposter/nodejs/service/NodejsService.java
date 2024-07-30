package com.catpawdogpaw.theartimposter.nodejs.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.catpawdogpaw.theartimposter.nodejs.entity.dto.GameRoomSTNDTO;
import com.catpawdogpaw.theartimposter.nodejs.entity.dto.GameSettingSTNDTO;
import com.catpawdogpaw.theartimposter.nodejs.entity.dto.PlayerSTNDTO;
import com.catpawdogpaw.theartimposter.nodejs.entity.dto.STNDTO;
import com.catpawdogpaw.theartimposter.nodejs.entity.dto.SubjectSTNDTO;

@Service
public class NodejsService {

	@Autowired
	private RestTemplate restTemplate;

	private PlayerSTNDTO createPlayerSTNDTO() {
		PlayerSTNDTO playerSTNDTO = new PlayerSTNDTO();
		playerSTNDTO.setPlayerId(1000);
		playerSTNDTO.setNickName(String.valueOf(0) + "playerNickName");
		playerSTNDTO.setGameCnt(0);
		playerSTNDTO.setVicCnt(0);
		playerSTNDTO.setUserId(0);
		playerSTNDTO.setProfileImage("imagePath"+0);
		return playerSTNDTO;
	}
	
	private List<PlayerSTNDTO> createOtherPlayerSTNDTOList(){
		List<PlayerSTNDTO> otherPlayerSTNDTOList = new ArrayList<>();
		
		for(int i = 1; i< 5; ++i) {
			PlayerSTNDTO playerSTNDTO = new PlayerSTNDTO();
			playerSTNDTO.setPlayerId(i);
			playerSTNDTO.setNickName(String.valueOf(i) + "playerNickName");
			playerSTNDTO.setGameCnt(i*2);
			playerSTNDTO.setVicCnt(i/2);
			playerSTNDTO.setUserId(i);
			playerSTNDTO.setProfileImage("imagePath"+i);
			otherPlayerSTNDTOList.add(playerSTNDTO);
		}
		
		return otherPlayerSTNDTOList;
	}
	
	private GameRoomSTNDTO createGameRoomSTNDTO() {
		GameRoomSTNDTO gameRoomSTNDTO = new GameRoomSTNDTO();
		gameRoomSTNDTO.setGameRoomId(1);
		gameRoomSTNDTO.setTitle("TestRoom");
		
		return gameRoomSTNDTO;
	}
	
	private GameSettingSTNDTO createGameSettingSTNDTO() {
		GameSettingSTNDTO gameSettingSTNDTO = new GameSettingSTNDTO();
		gameSettingSTNDTO.setMinPeople(5);
		gameSettingSTNDTO.setMaxPeople(5);
		gameSettingSTNDTO.setRoundTimeLimit(500);
		gameSettingSTNDTO.setTurnTimeLimit(30);
		gameSettingSTNDTO.setVersion("1.1.1.1");
		gameSettingSTNDTO.setRound(3);
		return gameSettingSTNDTO;
	}
	
	private List<SubjectSTNDTO> createSubjectSTNDTO(){
		List<SubjectSTNDTO> subjectSTNDTOList = new ArrayList<>();
		for(int i = 0 ; i < 5; ++i) {
			SubjectSTNDTO subjectSTNDTO = new SubjectSTNDTO();
			subjectSTNDTO.setSubjectId(i);
			subjectSTNDTO.setCategory("Number");
			subjectSTNDTO.setSubject(String.valueOf(i));
			subjectSTNDTOList.add(subjectSTNDTO);
		}
		
		return subjectSTNDTOList;
	}
	
	public STNDTO createSTNDTO() {
		STNDTO sTNDTO = new STNDTO();

//		sTNDTO.setPlayer(createPlayerSTNDTO());
//		sTNDTO.setOtherPlayerList(createOtherPlayerSTNDTOList());
		sTNDTO.setGameRoom(createGameRoomSTNDTO());
		sTNDTO.setGameSetting(createGameSettingSTNDTO());
		sTNDTO.setSubjectList(createSubjectSTNDTO());
		
		return sTNDTO;
	}
	
    public void sendToNode(STNDTO stndto) {
        String nodeServerUrl = "http://localhost:3000/receive-data"; // Node.js  URL
        restTemplate.postForObject(nodeServerUrl, stndto, Void.class);
    }


	
}
