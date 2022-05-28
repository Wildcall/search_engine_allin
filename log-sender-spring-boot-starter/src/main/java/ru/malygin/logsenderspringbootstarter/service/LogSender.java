package ru.malygin.logsenderspringbootstarter.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class LogSender {

    private final RabbitTemplate rabbitTemplate;
    @Value(value = "${spring.application.name}")
    private String appName;
    @Value(value = "${rabbit.queues.log-queue}")
    private String logQueueName;

    public LogSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Bean
    public Queue getLogQueue() {
        return new Queue(logQueueName, false, false, false);
    }

    public void info(String s,
                     Object... o) {
        if (o != null && s != null) {
            String msg = String.format(s, o);
            String log = String.format("Application: %s / Time: %s / Message: {%s}", appName, LocalDateTime.now(), msg);
            send(log.getBytes(StandardCharsets.UTF_8), 0);
        }
    }

    public void error(String s,
                      Object... o) {
        if (o != null && s != null) {
            String msg = String.format(s, o);
            String log = String.format("Application: %s / Time: %s / Message: {%s}", appName, LocalDateTime.now(), msg);
            send(log.getBytes(StandardCharsets.UTF_8), 1);
        }
    }

    private void send(byte[] msg,
                      int code) {
        MessageProperties mp = new MessageProperties();
        mp.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        if (code == 0) {
            mp.setHeader("type", "info");
        }
        if (code == 1) {
            mp.setHeader("type", "error");
        }
        Message message = new Message(msg, mp);
        rabbitTemplate.send(logQueueName, message);
    }
}
