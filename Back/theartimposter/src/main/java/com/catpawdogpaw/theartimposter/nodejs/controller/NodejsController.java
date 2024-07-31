package com.catpawdogpaw.theartimposter.nodejs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.catpawdogpaw.theartimposter.nodejs.entity.dto.STNDTO;
import com.catpawdogpaw.theartimposter.nodejs.service.NodejsService;

@Controller
@RequestMapping("/Nodejs")
public class NodejsController {

	@Autowired
	private NodejsService nodejsService;
	
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
    
//    @PostMapping("/result")
  
}
