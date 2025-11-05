package com.BTL_JAVA.BTL.configuration;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {
    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration configuration = new com.corundumstudio.socketio.Configuration();
        configuration.setPort(8099);
        configuration.setOrigin("*");
//        return new SocketIOServer(configuration);

        SocketIOServer server = new SocketIOServer(configuration);
        server.start();
        System.out.println("✅ Socket.IO server started on port 8099");
        return server;
    }
}
