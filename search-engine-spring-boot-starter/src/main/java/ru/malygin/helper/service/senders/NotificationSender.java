package ru.malygin.helper.service.senders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import ru.malygin.helper.model.Notification;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class NotificationSender {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper;
    private final Queue queue;
    @Value(value = "${spring.application.name}")
    private String appName;

    public void send(Notification n) {
        try {
            byte[] body = mapper
                    .writeValueAsString(n)
                    .getBytes();
            Message message = MessageBuilder
                    .withBody(body)
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .setTimestamp(new Date(System.currentTimeMillis()))
                    .setHeader("__TypeId__", "Notification")
                    .setHeader("app", appName)
                    .build();

            rabbitTemplate.send(queue.getName(), message);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
