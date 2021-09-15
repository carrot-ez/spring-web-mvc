package kr.carrot.springwebmvc.message.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaSender kafkaSender;

    @GetMapping("message/kafka/send")
    public void sendMessage() {

        kafkaSender.sendMessage();
    }
}
