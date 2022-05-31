package ru.malygin.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import ru.malygin.helper.config.SearchEngineProperties.Msg.BaseMsg;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public class MsgQueueDeclareFactory {

    private final RabbitAdmin rabbitAdmin;
    private final Map<String, Queue> queuesMap = new ConcurrentHashMap<>();
    private final Map<String, Exchange> exchangesMap = new ConcurrentHashMap<>();

    public Queue createQueue(BaseMsg msg) {
        String queueName = msg.getQueue();
        String exchangeName = msg.getExchange();
        Queue queue = declareQueue(queueName);
        Exchange exchange = declareExchange(exchangeName);

        if (queue != null && exchange != null) {
            rabbitAdmin.declareBinding(BindingBuilder
                                               .bind(queue)
                                               .to(exchange)
                                               .with(queue.getName())
                                               .noargs());
            log.info("[*] Binding queue '{}' and exchange '{}'", queue.getName(), exchange.getName());
        }
        return queue;
    }

    private Queue declareQueue(String queueName) {
        if (queueName == null) return null;

        Queue queue = queuesMap.get(queueName);

        if (queue != null) return queue;

        queue = new Queue(queueName, false, false, false);
        rabbitAdmin.declareQueue(queue);
        queuesMap.put(queueName, queue);
        log.info("[*] Declare queue '{}'", queue.getName());
        return queue;
    }

    private Exchange declareExchange(String exchangeName) {
        if (exchangeName == null) return null;

        Exchange exchange = exchangesMap.get(exchangeName);

        if (exchange != null) return exchange;

        exchange = new DirectExchange(exchangeName, false, false);
        rabbitAdmin.declareExchange(exchange);
        exchangesMap.put(exchangeName, exchange);
        log.info("[*] Declare exchange '{}'", exchange.getName());
        return exchange;
    }
}
