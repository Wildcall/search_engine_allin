package ru.malygin.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Listener {

    @RabbitListener(
            queuesToDeclare = @Queue(
                    name = "${rabbit.queues.log-queue}",
                    durable = "false",
                    autoDelete = "false"))
    public void receive(Message message) {
        log.info(new String(message.getBody()));
    }
}
