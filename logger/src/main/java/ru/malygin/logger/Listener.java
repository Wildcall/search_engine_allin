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
        String type = message
                .getMessageProperties()
                .getHeader("type");
        if ("error".equals(type)) {
            log.error(new String(message.getBody()));
            return;
        }
        if ("info".equals(type)) {
            log.info(new String(message.getBody()));
            return;
        }
        log.debug(new String(message.getBody()));
    }
}
