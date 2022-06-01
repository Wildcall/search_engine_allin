package ru.malygin.logger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import ru.malygin.helper.config.SearchEngineProperties;
import ru.malygin.helper.model.LogReceiveEvent;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class LogReceiverService {

    static {
        log.info("[o] Create LogReceiver in application");
    }

    private final ApplicationEventPublisher publisher;
    private final SearchEngineProperties seProp;

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
