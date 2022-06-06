package ru.malygin.taskmanager.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import ru.malygin.helper.model.TaskCallback;

public interface TaskCallbackReceiverService {
    @RabbitListener(queues = "#{properties.getCommon().getCallback().getRoute()}")
    void receiveTaskCallback(TaskCallback taskCallback);
}
