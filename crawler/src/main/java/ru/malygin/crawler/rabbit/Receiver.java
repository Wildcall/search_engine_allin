package ru.malygin.crawler.rabbit;

import org.springframework.amqp.core.Message;

public interface Receiver {
    void receive(Message message);
}
