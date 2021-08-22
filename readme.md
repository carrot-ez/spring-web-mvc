# Spring-web-mvc

# 8 Message queue

## JMS
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

## RabbitMQ / AMQP

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

### dependency

```gradle
implementation 'org.springframework.boot:spring-boot-starter-amqp'
```

### properties

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

### `RabbitTemplate` methods

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

### `MessageConvert` 

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

### rabbitmq config

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



### send message

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

### recieve message

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



## Kafka 

메시지 브로커이다.

- Cluster
  - 여러 개의 브로커로 구성된다.
  - 각 브로커는 토픽의 파티션 리더로 동작한다.
- Topic
- Partition

카프카의 토픽은 클러스터의 모든 브로커에 걸쳐 복제된다.

클러스터의 각 노드는 하나 이상의 토픽에 대한 리더로 동작한다. 토픽데이터를 관리하고 클러스터의 다른 노드로 데이터를 복제한다.

### dependency

```gradle
implementation 'org.springframework.kafka:spring-kafka'
```

### `application.yml`

```yaml
spring:
  kafka:
  
    # kafka 서버들의 위치 설정
    bootstrap-servers: 
    - xxx.com:9092
    - xxx.com:9093
    
    template:
      default-topic: ... # set default topic
```

### methods

`ListenableFuture<SendResult<K,V>> send(...)`

`ListenableFuture<SendResult<K, V>> sendDefault(...)`

- default topic을 지정했을 시 topic을 적지 않아도 됨

### send message

```java
public void sendMessage(HelloDate data) {
    kafkaTemplate.send("kr.carrot.topic", data);
}
```

### recieve message

```java
@KafkaListener(topics = "kr.carrot.topic", groupId = "group.carrot")
public void receiveMessage(String message) {
    System.out.println("KafkaReceiver.receiveMessage");

    System.out.println(message);
}
```

- `@KafkaListener`를 이용하여 메세지를 수신한다.



# 9 스프링 통합

### dependency

```build.gradle
implementation 'org.springframework.boot:spring-boot-starter-integration';
// 스프링 통합의 엔드포인트 모듈
implementation 'org.springframework.integration:spring-integration-file';
```

### Gateway Interface

```java
@MessagingGateway(defaultRequestChannel = "textInChannel")
public interface FileWriterGateway {

    void writeToFile(@Header(FileHeaders.FILENAME) String filename, String data);
}
```

- `@MessagingGateway`
  - 인터페이스의 구현체를 런타임시 생성하라고 스프링에 알려준다.
  - `@Repository` 의 구현체를 스프링이 생성하는것과 유사하다.
- `defaultRequestChannel`
  - 생성된 메시지를 속성에 지정된 메시지 채널로 전송한다.

### Components

- `Channel`
  - 한 요소로부터 다른 요소로 메시지 전달
- `Filter`
  - 조건에 맞는 메시지만 플로우를 통과하도록 함
- `Transformer`
  - 메시지 값 또는 타입을 변환
- `Router`
  - 여러 채널 중 하나로 메시지를 전달
  - 보통 메시지 헤더를 기반으로 동작
- `Splitter`
  - 들어오는 메시지를 두 개 이상의 메시지로 분할하여 각각 다른 채널로 전송
- `Aggregator`
  - `Splitter`와 상반된 기능을 함
  - 별개의 채널로부터 전달되는 다수의 메시지를 하나의 메시지로 결합
- `Service activator`
  - 메시지를 처리하도록 자바 메서드에 메시지를 넘겨준 후 반환값을 출력 채널로 전송한다.
- `Channel adapter`
  - 외부 시스템에 채널을 연결한다.
  - 외부 시스템으로부터 입력을 받거나 쓸 수 있다.
- `Gateway`
  - 인터페이스를 통해 통합 플로우로 데이터를 전달한다.

## MessageChannel

### 구현체

- `PublishSubscribeChannel`
- `QueueChannel`
- `PriorityChannel`
- `RendezvousChannel`
- `DirectChannel`
- `ExecutorChannel`
- `FluxMessageChannel`

기본값으로 `DirectChannel`이 사용된다.

구현체 변경은 `@Bean`을 선언하고, 통합플로우에서 참조하면 된다.

> ```java
> @Bean
> public MessageChannel channelName() {
>     return new PublishSubscriberChannel();
> }
> ```

`QueueChannel` 설정시 반드시 컨슈머가 풀링하도록 설정해줘야한다.

### 자바 DSL 구성

```JAVA
@Bean
public IntegrationFlow flowName() {
    return IntegrationFlows
        ...
        .channel("channelName")
        ...
        .get();
}
```



## Filter

### 자바 DSL 구성

```java
@Bean
public IntegrationFlow flowName() {
    return IntegerationFlows
        ...
        .<Integer>filter(e -> e % 2 == 0) // 짝수만 통과시키는 필터
        ...
        .get();
}
```

- `filter` 메소드는 `GenericSelector` 함수형 인터페이스를 인자로 갖는다.



## Transformer

### 자바 DSL 구성

```java
@Bean
public CustomTransformer customTransformer() {
    // GenericTransformer 인터페이스를 직접 구현한 클래스(구현체)
    return new CustomTransformer(); 
}

@Bean
public IntegrationFlow flowName() {
    return IntegrationFlows
        ...
        .transform(customTransformer)
        ...
        .get();
}
```

- `GenericTransformer` 인터페이스를 구현한 구현체를 지정한다.



## Router

### 자바 DSL 구성

1. `AbstractMessageRouter`  빈 생성

   ```java
   @Bean
   public AbstractMessageRouter evenOddRouter() {
       // 홀수, 짝수에 따라 다르게 라우팅
       return new AbstractMessageRouter() {
           
           @Override
           protected Collection<MessageChannel> determineTargetChannels(Message<?> message) {
               
               Integer number = (Integer) message.getPayload();
               if(number % 2 == 0) {
                   return Collections.singleton(evenChannel());
               }
               else {
                   return Collections.singleton(oddChannel());
               }
           }
       }
   }
   
   @Bean
   public IntegrationFlow flowName() {
       return IntegrationFlows
           ...
           .route(evenOddRouter)
           ...
           .get();
   }
   ```

2. lambda 이용

   ```java
   @Bean
   public IntegrationFlow flowName() {
       return IntegrationFlows
           ...
           .<Integer, String>route(
       		n -> n % 2 == 0 ? "EVEN" : "ODD",
           	mapping -> mapping
           		.subFlowMapping("EVEN", sf -> sf.transform(...).handle(...))
           		.subFlowMapping("ODD", sf -> sf.transform(...).handle(...))
       	)
           ...
           .get();
   }
   ```



## Splitter

### POJO class 정의

```java
public class OrderSplitter {
    
   	public Collection<Object> splitOrderIntoParts(PurchaseOrder po) {
        ArrayList<Object> parts = new ArrayList<>();
        parts.add(po.getBillingInfo());
        parts.add(po.getLineItems());
        
        return parts;
    }
}
```

### Bean 으로 선언

```java
@Bean
public OrderSplitter orderSplitter() {
    return new OrderSplitter();
}
```

### 자바 DSL

```java
@Bean
public IntegrationFlow flowName() {
    return IntegrationFlows
        ...
        .split(orderSplitter())
        .<Object, String>route(
    		p -> {
                if (p.getClass().isAssignableFrom(BillingInfo.class))
                    return "BILLING_INFO";
                else
                    return "LINE_INFO";
            },
        	mapping -> mapping
        		.subFlowMapping("BILLING_INFO", sf -> ...)
        		.subFlowMapping("LINE_IINFO", sf -> ...)
    	)
        ...
        .get();
}
```

