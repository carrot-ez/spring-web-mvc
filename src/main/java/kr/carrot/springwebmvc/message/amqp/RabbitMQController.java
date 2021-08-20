package kr.carrot.springwebmvc.message.amqp;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RabbitMQController {

    private final RabbitMQSender rabbitMQSender;

    @GetMapping("message/amqp/send")
    public void sendMessage() {
        rabbitMQSender.sendMessage();
    }

    @GetMapping("message/amqp/send-convert")
    public void convertAndSendMessage() {
        rabbitMQSender.convertAndSendMessage();
    }

}
