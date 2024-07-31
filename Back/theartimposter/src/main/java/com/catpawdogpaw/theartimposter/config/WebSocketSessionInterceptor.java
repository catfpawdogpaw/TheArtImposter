package com.catpawdogpaw.theartimposter.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.catpawdogpaw.theartimposter.match.MatchHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketSessionInterceptor implements HandshakeInterceptor {
	 
	private final MatchHandler matchHandler;
	 
	 private static final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof org.springframework.http.server.ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((org.springframework.http.server.ServletServerHttpRequest) request).getServletRequest();
            HttpSession session = servletRequest.getSession();
            if (session != null) {
                attributes.put("sessionId", session.getId());
                log.info("WebSocket beforeHandshake - sessionId: " + session.getId());
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("WebSocket afterHandshake");
        
    }

    public static WebSocketSession getSessionById(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public static void addSession(String sessionId, WebSocketSession session) {
        sessionMap.put(sessionId, session);
    }

    public static void removeSession(String sessionId) {
        sessionMap.remove(sessionId);
    }

    public static Map<String, WebSocketSession> getSessionMap() {
        return sessionMap;
    }
    
       
    
}
