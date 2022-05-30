package ru.malygin.helper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.ApplicationEventPublisher;
import ru.malygin.helper.model.TaskAction;
import ru.malygin.helper.model.TaskReceiveEvent;

@RequiredArgsConstructor
public class TaskReceiver<T> implements MessageListener {

    private final Jackson2JsonMessageConverter converter;
    private final ApplicationEventPublisher publisher;

    @Override
    public void onMessage(Message message) {
        T t = (T) converter.fromMessage(message);
        String header = message
                .getMessageProperties()
                .getHeader("action");
        TaskAction action = TaskAction.UNKNOWN;
        if (header != null) {
            try {
                action = TaskAction.valueOf(header);
            } catch (IllegalArgumentException ignored) {
            }
        }
        publisher.publishEvent(new TaskReceiveEvent<>(t, action));
    }
}
