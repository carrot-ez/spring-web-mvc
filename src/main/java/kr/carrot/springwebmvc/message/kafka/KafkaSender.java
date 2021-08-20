package kr.carrot.springwebmvc.message.kafka;

import kr.carrot.springwebmvc.servlet.vo.HelloData;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaSender {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage() {
        System.out.println("KafkaSender.sendMessage");


        kafkaTemplate.send("kr.carrot.topic", "hello world");
    }
}
