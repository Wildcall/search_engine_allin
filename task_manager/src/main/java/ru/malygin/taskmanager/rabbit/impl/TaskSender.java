package ru.malygin.taskmanager.rabbit.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.malygin.taskmanager.config.RabbitMQConfig;
import ru.malygin.taskmanager.rabbit.Sender;

@Slf4j
@Service
public class TaskSender implements Sender {

    private final RabbitMQConfig rabbitMQConfig;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public TaskSender(RabbitMQConfig rabbitMQConfig,
                      RabbitTemplate rabbitTemplate,
                      ObjectMapper objectMapper) {
        this.rabbitMQConfig = rabbitMQConfig;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        objectMapper.findAndRegisterModules();
    }

    @Override
    public void send(Object o) {
        if (o != null) {
            try {
                byte[] body = objectMapper
                        .writeValueAsString(o)
                        .getBytes();
                Message message =
                        MessageBuilder
                                .withBody(body)
                                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                                .setHeader("__TypeId__", "Task")
                                .setHeader("action", "START")
                                .build();
                rabbitTemplate.send(rabbitMQConfig.getTaskQueueName(), message);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
    }
}
