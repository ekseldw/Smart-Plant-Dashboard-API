package icns.smartplantdashboardapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/send", "/alert");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
	// registry.addEndpoint("/ws").setAllowedOrigins("http://192.168.219.10").withSockJS();
        // registry.addEndpoint("/ws").setAllowedOrigins("http://happycom.icnslab.net:8280").withSockJS();
	// registry.addEndpoint("/ws").setAllowedOrigins("http://211.226.15.58:8280", "http://192.168.219.10", "http://happycom.icnslab.net:8280", "http://192.168.219.10:8080").withSockJS();
	//registry.addEndpoint("/ws").setAllowedOrigins("http://115.90.76.67:8280", "http://192.168.0.39", "http://192.168.0.39:8080").withSockJS();
	registry.addEndpoint("/ws").setAllowedOrigins("http://115.90.76.67:8280", "http://192.168.0.39", "http://192.168.0.39:8080", "http://happycom.icnslab.net:8280", "http://163.180.117.169:8080").withSockJS();
    }
}
