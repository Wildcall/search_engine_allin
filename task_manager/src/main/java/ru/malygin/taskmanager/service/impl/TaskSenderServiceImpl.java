package ru.malygin.taskmanager.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.malygin.taskmanager.config.TaskManagerProperties.ServiceQueueProperties;
import ru.malygin.taskmanager.service.TaskSenderService;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskSenderServiceImpl implements TaskSenderService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper;

    @Override
    public void send(Map<String, Object> map,
                     ServiceQueueProperties serviceQueueProperties,
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
                        .build();
                rabbitTemplate.send(serviceQueueProperties.getExchange(),
                                    serviceQueueProperties.getRoute(),
                                    message);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
    }
}
