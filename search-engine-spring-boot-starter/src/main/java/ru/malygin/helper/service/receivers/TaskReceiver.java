package ru.malygin.helper.service.receivers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;

import java.util.Map;

public interface TaskReceiver {

    @RabbitListener(queues = "#{properties.getCommon().getTask().getRoute()}")
    void receiveTask(Map<String, Object> taskMap,
                     @Header("action") String action);
}
