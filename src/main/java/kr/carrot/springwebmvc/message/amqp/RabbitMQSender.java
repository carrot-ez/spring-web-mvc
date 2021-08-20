package kr.carrot.springwebmvc.message.amqp;

import kr.carrot.springwebmvc.servlet.vo.HelloData;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQSender {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage() {

        System.out.println("RabbitMQSender.sendMessage");

        HelloData helloData = new HelloData();
        helloData.setAge(26);
        helloData.setUsername("carrot");

        MessageConverter messageConverter = rabbitTemplate.getMessageConverter();
        MessageProperties props = new MessageProperties(); // org.springframework.amqp.core.MessageProperties
        props.setHeader("X-CARROT", "KEY");

        Message message = messageConverter.toMessage(helloData, props);
        rabbitTemplate.send("kr.carrot", "key.#", message);
    }

    public void convertAndSendMessage() {

        System.out.println("RabbitMQSender.convertAndSendMessage");

        HelloData helloData = new HelloData();
        helloData.setAge(26);
        helloData.setUsername("carrot");

        rabbitTemplate.convertAndSend("kr.carrot", "key.carrot", helloData,
                new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        MessageProperties props = message.getMessageProperties();
                        props.setHeader("X-CARROT", "KEY");
                        return message;
                    }
                });
    }
}
