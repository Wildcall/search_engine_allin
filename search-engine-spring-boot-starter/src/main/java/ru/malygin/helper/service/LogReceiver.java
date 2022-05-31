package ru.malygin.helper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.context.ApplicationEventPublisher;
import ru.malygin.helper.model.LogReceiveEvent;

@RequiredArgsConstructor
public class LogReceiver implements MessageListener {

    private final ApplicationEventPublisher publisher;

    @Override
    public void onMessage(Message message) {
        String type = message
                .getMessageProperties()
                .getHeader("type");
        String msg = new String(message.getBody());
        publisher.publishEvent(new LogReceiveEvent(msg, type));
    }
}
