package ru.malygin.helper.service.receivers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.util.Assert;
import ru.malygin.helper.model.TaskAction;
import ru.malygin.helper.model.TaskReceiveEvent;
import ru.malygin.helper.service.receivers.TaskReceiver;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class DefaultTaskReceiver implements TaskReceiver {

    private final ApplicationEventPublisher publisher;
    private final ObjectMapper objectMapper;
    private final Map<String, Class<?>> idClassMap;

    @Override
    public void receiveTask(Map<String, Object> taskMap,
                            @Header("action") String action) {
        Assert.notNull(taskMap, "Task map must not be null");
        Assert.notNull(action, "Task action header must not be null");
        Class<?> aClass = idClassMap.get("Task");
        Assert.notNull(aClass, "Id class map must be contain Task.class");
        try {
            TaskAction taskAction = TaskAction.valueOf(action);
            Object o = objectMapper.convertValue(taskMap, aClass);
            publisher.publishEvent(new TaskReceiveEvent(o, taskAction));
        } catch (IllegalArgumentException e) {
            log.error("Task action not available or can't convert task map to {}: {}", action, aClass.getName());
        }
    }
}
