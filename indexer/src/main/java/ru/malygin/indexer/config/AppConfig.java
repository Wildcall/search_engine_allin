package ru.malygin.indexer.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.malygin.helper.service.DefaultQueueDeclareService;
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
    private final static String RESOURCE_EXCHANGE = "resource-exchange";

    @Bean
    public Map<String, Class<?>> idClassMap() {
        Map<String, Class<?>> map = new HashMap<>();
        map.put("Task", Task.class);
        map.put("Page", Page.class);
        log.info("[o] Configurate idClassMap in application");
        return map;
    }

    @Bean
    public boolean declareQueue(DefaultQueueDeclareService defaultQueueDeclareService) {
        defaultQueueDeclareService.createQueue(PAGE_REQUEST_QUEUE, RESOURCE_EXCHANGE);
        return true;
    }

    //
//    @Bean
//    public Queue declareRPC(DefaultQueueDeclareService queueFactory) {
//        return queueFactory.createQueue(PAGE_REQUEST_QUEUE, RPC_EXCHANGE);
//    }
//
//    @Bean
//    public TempListenerContainerFactory tempListenerContainerFactory(SimpleRabbitListenerContainerFactory f,
//                                                                     RabbitListenerEndpointRegistry r,
//                                                                     RabbitAdmin a) {
//        return new TempListenerContainerFactory(f, r, a);
//    }
//
    @Bean
    public PageRequestSender pageRequestSender(RabbitTemplate rabbitTemplate) {
        return new PageRequestSender(rabbitTemplate);
    }

    @RequiredArgsConstructor
    public static class PageRequestSender {

        private final RabbitTemplate rabbitTemplate;
        private final Random random = new Random();
        private final Map<Long, String> callbackAddress = new ConcurrentHashMap<>();

        public boolean sendRequest(Long appUserId,
                                   Long siteId,
                                   Long taskId) {
            log.info("Request pages from crawler");
            String pageQueue = "page-queue" + taskId + "==" + random.nextLong();
            Long pagesCount = (Long) rabbitTemplate.convertSendAndReceive(RESOURCE_EXCHANGE,
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
            return true;
        }
    }
//
//    @RequiredArgsConstructor
//    public static class TempListenerContainerFactory {
//        private final SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory;
//        private final RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;
//        private final RabbitAdmin rabbitAdmin;
//
//        public void create(String queueName) {
//            log.info("[o] Create TempMessageListenerContainer in application");
//            rabbitAdmin.declareQueue(new Queue(queueName, false, false, true));
//            SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
//            endpoint.setQueueNames(queueName);
//            endpoint.setId(queueName);
//            endpoint.setMessageListener(new ListenerReply());
//            rabbitListenerEndpointRegistry.registerListenerContainer(endpoint, rabbitListenerContainerFactory, true);
//        }
//    }
//
//    public static class ListenerReply implements MessageListener {
//
//        @Override
//        public void onMessage(Message message) {
//            log.info(message.toString());
//        }
//    }
}
