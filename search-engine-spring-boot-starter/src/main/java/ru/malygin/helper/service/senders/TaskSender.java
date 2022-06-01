package ru.malygin.helper.service.senders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class TaskSender {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper;
    @Value(value = "${spring.application.name}")
    private String appName;

    public void send(Map<String, Object> map,
                     String queue,
                     String action) {
        if (map != null) {
            try {
                byte[] body = mapper
                        .writeValueAsString(map)
                        .getBytes();
                Message message = MessageBuilder
                        .withBody(body)
                        .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                        .setHeader("__TypeId__", "Hashtable")
                        .setHeader("action", action)
                        .setHeader("app", appName)
                        .build();

                rabbitTemplate.send(queue, message);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
    }
}