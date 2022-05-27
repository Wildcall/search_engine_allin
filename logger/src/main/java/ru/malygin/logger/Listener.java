package ru.malygin.logger;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Listener {

    @SneakyThrows
    @RabbitListener(queues = "log-queue")
    public void receive(Message message) {
        String string = new String(message.getBody());
        log.info(" [x] Received '{}'", string);
    }
}
