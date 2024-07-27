package com.catpawdogpaw.theartimposter.match;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MatchController {
	 private final MatchHandler matchHandler;

	    @MessageMapping("/waitroom/join")
	    @SendTo("/wait-service/waitroom")
	    public String join(@Payload String message, SimpMessageHeaderAccessor headerAccessor) {

	    	WebSocketSession session = (WebSocketSession) headerAccessor.getSessionAttributes().get("session");
	         if (session != null) {
	             matchHandler.addUser(session);
	         }
	         log.info("Received message: " + message);
	         return message;
	    }
}
