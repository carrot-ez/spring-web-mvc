package kr.carrot.springwebmvc.message.jms;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JmsController {

    private final JmsSender jmsSubscriber;


    @GetMapping("message/jms")
    public String sendMessage() {

        jmsSubscriber.sendTestMessage();

        return "send message";
    }
}
