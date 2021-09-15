package kr.carrot.springwebmvc.message.jms;

import kr.carrot.springwebmvc.servlet.vo.HelloData;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JmsSender {

    private final JmsTemplate jmsTemplate;

    public void sendTestMessage() {

        HelloData helloData = new HelloData();
        helloData.setUsername("carrot");
        helloData.setAge(26);

        jmsTemplate.convertAndSend("mailbox", helloData);
    }

}
