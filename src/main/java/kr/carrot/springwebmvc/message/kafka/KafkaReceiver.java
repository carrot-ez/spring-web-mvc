package kr.carrot.springwebmvc.message.kafka;

import kr.carrot.springwebmvc.servlet.vo.HelloData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaReceiver {

    @KafkaListener(topics = "kr.carrot.topic", groupId = "group.carrot")
    public void receiveMessage(String message) {
        System.out.println("KafkaReceiver.receiveMessage");

        System.out.println(message);
//        System.out.println(record);
    }
}
