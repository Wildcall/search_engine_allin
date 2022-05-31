package ru.malygin.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;

@Slf4j
@RequiredArgsConstructor
public class MsgReceiverFactory {

    private final SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory;
    private final RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    public MessageListener create(Queue queue,
                                  MessageListener listener) {
        SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
        endpoint.setQueueNames(queue.getName());
        endpoint.setId(queue.getName());
        endpoint.setMessageListener(listener);
        rabbitListenerEndpointRegistry.registerListenerContainer(endpoint, rabbitListenerContainerFactory, true);
        log.info("[*] Create message listener with id '{}'", endpoint.getId());
        return listener;
    }
}
