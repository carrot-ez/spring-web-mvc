package kr.carrot.springwebmvc.message.amqp;

import kr.carrot.springwebmvc.servlet.vo.HelloData;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQReceiver {

    private final RabbitTemplate rabbitTemplate;

    /* pull model */
    public void receiveMessageWithPullModel() {

        System.out.println("RabbitMQReceiver.receiveMessageWithPullModel");

        Message message = rabbitTemplate.receive("kr.carrot.queue", 3000);
        MessageConverter messageConverter = rabbitTemplate.getMessageConverter();
        if (message != null) {
            Object obj = messageConverter.fromMessage(message);
            System.out.println(obj);
        }
        System.out.println("receive with pull model");
    }

    public void receiveAndConverMessageWithPullModel() {

        System.out.println("RabbitMQReceiver.receiveAndConverMessageWithPullModel");

        HelloData data = rabbitTemplate.receiveAndConvert("kr.carrot.queue", new ParameterizedTypeReference<HelloData>() {});

        System.out.println(data);
    }


    /* push model */
//    @RabbitListener(queues = "kr.carrot.queue")
    public void receiveMessageWithPushModel(final HelloData data) {
        System.out.println("RabbitMQReceiver.receiveMessageWithPushModel");

        System.out.println(data);
    }
}
