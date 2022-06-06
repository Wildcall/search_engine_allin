package ru.malygin.helper.service.senders.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import ru.malygin.helper.config.SearchEngineProperties;
import ru.malygin.helper.model.TaskAction;
import ru.malygin.helper.model.TaskState;
import ru.malygin.helper.service.senders.CallbackSender;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class DefaultCallbackSender implements CallbackSender {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper;
    private final SearchEngineProperties.Common.Callback cProp;

    @Override
    public void send(Long id,
                     TaskAction action,
                     TaskState state) {
        prepareMessage(Map.of("id", id,
                              "action", action,
                              "state", state)).ifPresent(message ->
                                                                 rabbitTemplate.send(cProp.getExchange(),
                                                                                     cProp.getRoute(), message));
    }

    private Optional<Message> prepareMessage(Map<String, Object> map) {
        try {
            byte[] body = mapper
                    .writeValueAsString(map)
                    .getBytes();
            return Optional.of(MessageBuilder
                                       .withBody(body)
                                       .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                                       .setTimestamp(new Date(System.currentTimeMillis()))
                                       .setHeader("__TypeId__", "Hashtable")
                                       .build());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }
}
