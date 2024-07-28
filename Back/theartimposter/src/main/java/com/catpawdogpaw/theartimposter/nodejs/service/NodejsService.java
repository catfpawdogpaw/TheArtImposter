package com.catpawdogpaw.theartimposter.nodejs.service;

import java.util.ArrayList;
import java.util.List;

import com.catpawdogpaw.theartimposter.nodejs.entity.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NodejsService {

	@Autowired
	private RestTemplate restTemplate;

	private List<PlayerSTNDTO> createPlayerSTNDTO(){
		List<PlayerSTNDTO> playerSTNDTOList = new ArrayList<>();
		
		for(int i = 0; i< 5; ++i) {
			PlayerSTNDTO playerSTNDTO = new PlayerSTNDTO();
			playerSTNDTO.setPlayerId(i);
			playerSTNDTO.setNickName(String.valueOf(i) + "playerNickName");
			playerSTNDTO.setGameCnt(i*2);
			playerSTNDTO.setVicCnt(i/2);
			playerSTNDTO.setUserId(i);
			playerSTNDTO.setProfileImage("imagePath"+i);
			playerSTNDTOList.add(playerSTNDTO);
		}
		
		return playerSTNDTOList;
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
		gameSettingSTNDTO.setRoundTimeLimit(500);
		gameSettingSTNDTO.setTurnTimeLimit(30);
		gameSettingSTNDTO.setVersion("1.1.1.1");
		
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
		
		sTNDTO.setPlayerList(createPlayerSTNDTO());
		sTNDTO.setGameRoom(createGameRoomSTNDTO());
		sTNDTO.setGameSetting(createGameSettingSTNDTO());
		sTNDTO.setSubjectList(createSubjectSTNDTO());
		
		return sTNDTO;
	}
	
    public void sendToNode(STNDTO stndto) {
        String nodeServerUrl = "http://localhost:3000/receive-data"; // Node.js
		// ������ URL
        restTemplate.postForObject(nodeServerUrl, stndto, Void.class);
    }
	
}
