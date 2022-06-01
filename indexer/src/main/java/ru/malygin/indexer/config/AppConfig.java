package ru.malygin.indexer.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.malygin.helper.MsgQueueDeclareFactory;
import ru.malygin.indexer.model.Page;
import ru.malygin.indexer.model.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Configuration
public class AppConfig {

    private final static String PAGE_REQUEST_QUEUE = "page-request-queue";
    private final static String RPC_EXCHANGE = "rpc-exchange";

    @Bean
    protected Map<String, Class<?>> idClassMap() {
        Map<String, Class<?>> map = new HashMap<>();
        map.put("Task", Task.class);
        map.put("Page", Page.class);
        return map;
    }

    @Bean
    public Queue declareRPC(MsgQueueDeclareFactory queueFactory) {
        return queueFactory.createQueue(PAGE_REQUEST_QUEUE, RPC_EXCHANGE);
    }

    @Bean
    public TempListenerContainerFactory tempListenerContainerFactory(SimpleRabbitListenerContainerFactory f,
                                                                     RabbitListenerEndpointRegistry r,
                                                                     RabbitAdmin a) {
        return new TempListenerContainerFactory(f, r, a);
    }

    @Bean
    public Client client(RabbitTemplate r,
                         TempListenerContainerFactory f) {
        return new Client(r, f);
    }

    @RequiredArgsConstructor
    public static class Client {

        private final RabbitTemplate rabbitTemplate;
        private final TempListenerContainerFactory factory;
        private final Random random = new Random();
        private final Map<Long, String> callbackAddress = new ConcurrentHashMap<>();

        public boolean send(Long appUserId,
                            Long siteId,
                            Long taskId) {
            log.info("Request pages from crawler");
            String pageQueue = "page-queue" + taskId + "==" + random.nextLong();
            Long pagesCount = (Long) rabbitTemplate.convertSendAndReceive(RPC_EXCHANGE,
                                                                          PAGE_REQUEST_QUEUE,
                                                                          Map.of("appUserId", appUserId.toString(),
                                                                                 "siteId", siteId.toString(),
                                                                                 "pageQueue", pageQueue));
            callbackAddress.put(taskId, pageQueue);
            if (pagesCount == null) {
                log.info("Got {}", pagesCount);
                return false;
            }
            log.info("Got {}", pagesCount);
            //factory.create(pageQueue);
            return true;
        }
    }

    @RequiredArgsConstructor
    public static class TempListenerContainerFactory {
        private final SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory;
        private final RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;
        private final RabbitAdmin rabbitAdmin;

        public void create(String queueName) {
            log.info("[o] Create TempMessageListenerContainer in application");
            rabbitAdmin.declareQueue(new Queue(queueName, false, false, true));
            SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
            endpoint.setQueueNames(queueName);
            endpoint.setId(queueName);
            endpoint.setMessageListener(new ListenerReply());
            rabbitListenerEndpointRegistry.registerListenerContainer(endpoint, rabbitListenerContainerFactory, true);
        }
    }

    public static class ListenerReply implements MessageListener {

        @Override
        public void onMessage(Message message) {
            log.info(message.toString());
        }
    }
}
