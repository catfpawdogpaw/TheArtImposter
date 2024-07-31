package com.catpawdogpaw.theartimposter.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class WebSocketEventListener {
	  private static final ConcurrentMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        // 연결 이벤트 처리
    	StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        WebSocketSession session = WebSocketSessionInterceptor.getSessionById(sessionId);
        if (session != null) {
            sessionMap.put(sessionId, session);
            log.info("WebSocket session added to sessionMap: {}", sessionId);
        } else {
            log.warn("WebSocket session not found for sessionId: {}", sessionId);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // 연결 해제 이벤트 처리
        String sessionId = event.getSessionId();
        WebSocketSessionInterceptor.removeSession(sessionId);
        log.info("WebSocket session removed from sessionMap: {}", sessionId);
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

    public static ConcurrentMap<String, WebSocketSession> getSessionMap() {
        return sessionMap;
    }
}
