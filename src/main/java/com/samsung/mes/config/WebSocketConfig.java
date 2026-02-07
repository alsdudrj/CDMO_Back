package com.samsung.mes.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 프론트에서 접속할 엔드포인트: ws://localhost:8080/ws-cdms
        registry.addEndpoint("/ws-cdms")
                .setAllowedOriginPatterns("*") // CORS 모두 허용
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 서버에서 클라이언트로 보낼 때 사용하는 경로 prefix
        registry.enableSimpleBroker("/topic");
        // 클라이언트에서 서버로 메시지 보낼 때 사용하는 경로 prefix
        registry.setApplicationDestinationPrefixes("/app");
    }
}