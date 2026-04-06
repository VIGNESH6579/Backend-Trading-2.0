package com.ocsentinel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/**
 * WebSocket Configuration for Browser Communication
 * 
 * This configuration enables WebSocket communication between the backend and browser clients.
 * The browser connects to this WebSocket to receive real-time updates from our REST polling service.
 * 
 * IMPORTANT: This is NOT the Angel One WebSocket that was causing network issues.
 * This is a local WebSocket for browser-backend communication only.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Browser connects here via SockJS for receiving REST polling updates
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
