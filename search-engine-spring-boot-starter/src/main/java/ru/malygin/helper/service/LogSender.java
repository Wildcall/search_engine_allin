package ru.malygin.helper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class LogSender {

    private final RabbitTemplate rabbitTemplate;
    private final String queue;
    @Value(value = "${spring.application.name}")
    private String appName;

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
        rabbitTemplate.send(queue, message);
    }
}
