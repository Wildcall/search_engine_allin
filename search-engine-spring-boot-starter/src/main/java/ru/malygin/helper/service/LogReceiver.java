package ru.malygin.helper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.Header;
import ru.malygin.helper.model.LogReceiveEvent;

import java.util.Map;

@RequiredArgsConstructor
public class LogReceiver {

    private final ApplicationEventPublisher publisher;

    @RabbitListener(queues = "#{seProp.getMsg().getLog().getQueue()}", id = "#{seProp.getMsg().getLog().getQueue()}")
    public void receiveLog(Map<String, String> map,
                           @Header("type") String type,
                           @Header("app") String app,
                           Message message) {
        map.put("Application", app);
        map.put("Time", String.valueOf(message
                                               .getMessageProperties()
                                               .getTimestamp()
                                               .getTime()));
        publisher.publishEvent(new LogReceiveEvent(map, type));
    }
}
