package com.catpawdogpaw.theartimposter.nodejs.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.catpawdogpaw.theartimposter.gameroom.GameRoomService;
import com.catpawdogpaw.theartimposter.nodejs.entity.dto.GameRoomResultDTO;
import com.catpawdogpaw.theartimposter.nodejs.entity.dto.STNDTO;
import com.catpawdogpaw.theartimposter.nodejs.service.NodejsService;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Controller
@RequestMapping("/Nodejs")
public class NodejsController {

	private final NodejsService nodejsService;
	private final GameRoomService gameRoomService;

	
	
	@GetMapping("/test-data")
    public void testSendToNode() {
        STNDTO stndto = nodejsService.createSTNDTO();
        nodejsService.sendToNode(stndto);
    }
	
    @PostMapping("/send-data")
    public void sendToNode() {
        STNDTO stndto = nodejsService.createSTNDTO();
        nodejsService.sendToNode(stndto);
    }
    
    @PostMapping("/result")
    public ResponseEntity<String> saveResult(@RequestBody GameRoomResultDTO gameRoomResultDTO) {	
    	//nodejsService.saveGameResult(gameRoomResultDTO);
    	return new ResponseEntity<>("Game result received successfully", HttpStatus.OK);
    
    }
  
}
