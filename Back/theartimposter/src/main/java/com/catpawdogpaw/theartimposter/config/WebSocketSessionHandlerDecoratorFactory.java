package com.catpawdogpaw.theartimposter.config;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import com.catpawdogpaw.theartimposter.match.MatchHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class WebSocketSessionHandlerDecoratorFactory implements WebSocketHandlerDecoratorFactory {

    private final MatchHandler matchHandler;

    @Override
    public WebSocketHandler decorate(WebSocketHandler handler) {
        return new WebSocketHandlerDecorator(handler) {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                String sessionId = session.getId();
                session.getAttributes().put("sessionId", sessionId);
                matchHandler.getSessionMap().put(sessionId, session);
                log.info("WebSocket connection established - sessionId: " + sessionId);
                super.afterConnectionEstablished(session);
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                String sessionId = session.getId();
                matchHandler.getSessionMap().remove(sessionId);
                log.info("WebSocket connection closed - sessionId: " + sessionId);
                super.afterConnectionClosed(session, closeStatus);
            }

            public void handleMessage(WebSocketSession session, TextMessage message) throws Exception {
                log.info("Received message: " + message.getPayload());
                super.handleMessage(session, message);
            }
        };
    }
}
