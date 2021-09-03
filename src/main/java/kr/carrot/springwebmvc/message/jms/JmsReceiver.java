package kr.carrot.springwebmvc.message.jms;

import kr.carrot.springwebmvc.servlet.vo.HelloData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsReceiver {

    @Autowired
    JmsTemplate jmsTemplate;


//    @JmsListener(destination = "mailbox", containerFactory = "myFactory")
//    @JmsListener(destination = "mailbox")
    public void receiveTestMessage(HelloData helloData) {

        System.out.println(helloData);
    }
}
