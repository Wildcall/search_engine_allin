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
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import ru.malygin.helper.config.SearchEngineProperties.Msg.BaseMsg;

import java.util.Date;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class LogSender {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper;
    private final BaseMsg logParam;
    @Value(value = "${spring.application.name}")
    private String appName;

    public void info(String s,
                     Object... o) {
        if (o != null && s != null) {
            String msg = String.format(s, o);
            send(msg, "info");
        }
    }

    public void error(String s,
                      Object... o) {
        if (o != null && s != null) {
            String msg = String.format(s, o);
            send(msg, "error");
        }
    }

    private void send(String msg,
                      String type) {
        try {
            byte[] body = mapper
                    .writeValueAsString(Map.of("message", msg))
                    .getBytes();

            Message message = MessageBuilder
                    .withBody(body)
                    .setTimestamp(new Date(System.currentTimeMillis()))
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .setHeader("type", type)
                    .setHeader("app", appName)
                    .build();
            rabbitTemplate.send(logParam.getExchange(), logParam.getQueue(), message);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onStart(ContextRefreshedEvent event) {
        info("Application start");
    }

    @EventListener(ContextClosedEvent.class)
    public void onStart(ContextClosedEvent event) {
        info("Application close");
    }
}
