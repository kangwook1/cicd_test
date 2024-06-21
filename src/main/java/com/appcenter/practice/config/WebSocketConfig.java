package com.appcenter.practice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// message-broker는 메시징 미들웨어로 어플리케이션 간에 통신을 수행을 함으로써, 서로 다른 언어나 플랫폼으로 개발되도 통신이 가능
// message-queue를 사용해 데이터 패킷을 순서대로 저장해 소비될 때까지 갖고 있어 손실을 방지하고, 장애가 발생해도 시스템 동작 가능
@Configuration
@EnableWebSocketMessageBroker // 웹소켓 메시지 핸들링 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    //stomp 사용(웹소켓 위에서 사용됨)- 클라이언트와 서버가 전송할 메세지의 유형, 형식, 내용들을 정의하는 매커니즘이다.
    //메세징 프로토콜과 메세징 형식을 개발할 필요가 없다.
    //중개 서버를 통한 클라이언트간에 비동기적 메시지 전송을 위한 프로토콜
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // sockJs는 기본적으로 웹소켓을 사용하지만 웹소켓을 지원하지 않는 브라우저는 대체 방법을 찾아주는 라이브러리이다.
        registry.addEndpoint("/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // handshake endpoint을 지정한다. 채팅 요청할 때 사용
    }

    //메시지 브로커 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //채팅방 구독 신청 접두사 URL 지정(채팅방 구독 요청)
        registry.enableSimpleBroker("/sub");
        //채팅 데이터를 전송할 때 마다 채팅방 구독자에게 메시지 브로커가 메시지를 보낼 때 URL 접두사 지정(메시지 발행 요청)
        registry.setApplicationDestinationPrefixes("/pub");
    }
}
