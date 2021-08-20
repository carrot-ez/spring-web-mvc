# Spring-web-mvc

## Message queue

### JMS
1. 메세지 브로커 의존성 추가 
   > ActiveMQ 사용
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

### RabbitMQ / AMQP

RabbitMQ

- AMQP의 가장 중요한 구현

AMQP

- 거래소 이름과 라우팅 키를 주소로 사용한다.

exchange(거래소)

- default
- direct
- topic
- fanout
- header
- dead letter

#### dependency

```gradle
implementation 'org.springframework.boot:spring-boot-starter-amqp'
```

#### properties

```yaml
# application.yml
spring:
  rabbitmq:
  	host: xxx.com # broker's host, default: localhost
  	port: 5673 # broker's port, default: 5672
  	username: # optional
  	password: # optional
  	template:
  	  exchange: # set default exchange
  	  routing-key: # set default routing key
  	  receive-timeout: # set recevier's timeout
```

#### `RabbitTemplate` methods

`void send(...)`

- 원시 `Message`  객체를 전송

`void convertAndSend(...)`

- 내부적으로 `Message`를 객체로 변환
- 브로커에게 전송되기 전 `Message` 객체를 조작하는데 사용될 수 있는 `MessagePostProcessor` 인자 포함 가능

`Message receive(...)`

- 원시 `Message` 객체 수신

`Object receiveAndConvert(...)`

- 메세지 수신 후 변환기를 사용하여 객체 변환

`<T> T receiveAndConvert(...)`

- type safe method

#### `MessageConvert` 

- `SimpleMessageConverter`
  - default message converter
  - String, byte[], Serializable 타입을 변환한다

- `Jackson2JsonMessageConverter`
  - 객체를 JSON 으로 상호 변환한다
- `MarshallingMessageConverter`
  - 스프링 `Marshaller`와 `Unmarshaller`를 사용하여 변환
- `SerializerMessageConverter`
  - 스프링 `Serialize`와 `Deserializer` 를 사용하여 변환
- `ContentTypeDelegationgMessageConverter`
  - contentType 헤더를 기반으로 다른 변환기에 변환을 위임한다.

#### rabbitmq config

```java
@Configuration
public class RabbitConfig {

    private static final String QUEUE_NAME = "kr.carrot.queue";
    private static final String EXCHANGE_NAME = "kr.carrot";
    private static final String ROUTING_KEY = "key.#";

    @Bean
    Queue queue() {
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter amqpJacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
```



#### send message

1. data

   ``` java
   HelloData helloData = new HelloData();
   helloData.setAge(26);
   helloData.setUsername("carrot");
   ```

2. use `send(...)`

   ```java
   // org.springframework.amqp.core.MessageProperties
   MessageProperties props = new MessageProperties(); 
   props.setHeader("X-CARROT", "KEY");
   
   MessageConverter messageConverter = rabbitTemplate.getMessageConverter();
   
   Message message = messageConverter.toMessage(helloData, props);
   rabbitTemplate.send("kr.carrot", "key.carrot", message);
   ```

3. use `converAndSend(...)`

   ```java
   rabbitTemplate.convertAndSend("kr.carrot", "key.#", helloData,
           new MessagePostProcessor() {
               @Override
               public Message postProcessMessage(Message message) throws AmqpException {
                   MessageProperties props = message.getMessageProperties();
                   props.setHeader("X-CARROT", "KEY");
                   return message;
               }
           });
   ```

#### recieve message

1. pull model

   ```java
   public void receiveAndConverMessageWithPullModel() {
   
       HelloData data = rabbitTemplate.receiveAndConvert("kr.carrot.queue", new ParameterizedTypeReference<HelloData>() {});
   
       System.out.println(data);
   }
   ```

2. push model

   ```java
   @RabbitListener(queues = "kr.carrot.queue")
   public void receiveMessageWithPushModel(final HelloData data) {
       
       System.out.println(data);
   }
   ```

   

