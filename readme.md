# Spring

---
## Message queue

### JMS
1. 메세지 브로커 의존성 추가 
   > ActiveMQ Artemis 사용
   ```gradle
    implementation 'org.springframework.boot:spring-boot-starter-activemq'
   ```
   
2. Message Converter 설정
   ```java
    /**
     * jms message converter
     * Spring Boot에서 제공하는 기본 컨버터가 Serializable Object만 변환해주므로 POJO 메세지 전송/수신시 필요하다.
     */
    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
   
        return converter;
    }
   ```
   
3. Sender / Receiver 작성
   ```java
   // Send
   jmsTemplate.convertAndSend("mailbox", message);
   
   // Receve
   @JmsListener(destination = "mailbox")
   public void receiveMessage(Mail message) {
        // do something
   }
   ```