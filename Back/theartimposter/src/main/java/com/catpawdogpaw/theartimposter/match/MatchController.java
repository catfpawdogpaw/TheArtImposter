package com.catpawdogpaw.theartimposter.match;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MatchController {
	 private final MatchHandler matchHandler;
	 private final SimpMessagingTemplate messagingTemplate;

	 
	 	@MessageMapping("/waitroom/sessions")
	    @SendTo("/wait-service/waitroom/sessions")
	    public List<String> getCurrentSessions() {
	        return matchHandler.getCurrentSessions();
	    }
	 
	    @MessageMapping("/waitroom/join")
	    @SendTo("/wait-service/waitroom")
	    public String join(@Payload String message, SimpMessageHeaderAccessor headerAccessor) {
	    	 logHeaders(headerAccessor);
	    	 
	    	 String sessionId = (String) headerAccessor.getSessionId();  	
	         if (sessionId != null) {
	        	 matchHandler.addUser(sessionId);
	             log.info("User with sessionId {} joined", sessionId);
	             
	             WebSocketSession session = matchHandler.getSessionById(sessionId);
	             if (session != null) {
	                 Map<String, Object> attributes = session.getAttributes();
	                 if (attributes.containsKey("gameRoomId")) {
	                     Long roomId = (Long) attributes.get("gameRoomId");
	                     String roomTitle = (String) attributes.get("gameRoomTitle");

	                     Map<String, Object> roomInfo = new HashMap<>();
	                     roomInfo.put("roomId", roomId);
	                     roomInfo.put("roomTitle", roomTitle);

	                     messagingTemplate.convertAndSend("/wait-service/waitroom", roomInfo);
	                 }
	             }
	             
	         } else {
	             log.warn("No sessionId found in headers");
	         }
	         log.info("join user message: " + message);
	         
	         // 클라이언트에게 메시지 푸시
	         messagingTemplate.convertAndSend("/wait-service/waitroom", "User joined: " + sessionId);

	         return message;
	    }
	    
	    private void logHeaders(SimpMessageHeaderAccessor headerAccessor) {
	        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
	        log.info("Session Attributes: " + sessionAttributes);

	        String sessionId = headerAccessor.getSessionId();
	        log.info("Session ID: " + sessionId);

	        Map<String, List<String>> nativeHeaders = headerAccessor.toNativeHeaderMap();
	        log.info("Native Headers: " + nativeHeaders);

	        log.info("Message Headers: " + headerAccessor.getMessageHeaders());
	    }
	    
	    private WebSocketSession getWebSocketSession(String sessionId) {
	        return matchHandler.getSessionById(sessionId);
	    }
	    
	    
	   
}
