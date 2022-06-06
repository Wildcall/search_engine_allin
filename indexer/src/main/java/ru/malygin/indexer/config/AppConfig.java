package ru.malygin.indexer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.malygin.helper.config.SearchEngineProperties;
import ru.malygin.helper.model.PageRequest;
import ru.malygin.helper.service.DefaultQueueDeclareService;
import ru.malygin.helper.service.DefaultTempListenerContainerFactory;
import ru.malygin.helper.service.senders.LogSender;
import ru.malygin.helper.service.senders.impl.DefaultPageRequestSender;
import ru.malygin.indexer.model.Page;
import ru.malygin.indexer.model.Task;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class AppConfig {

    @Bean
    public Map<String, Class<?>> idClassMap() {
        Map<String, Class<?>> map = new HashMap<>();
        map.put("Task", Task.class);
        map.put("Page", Page.class);
        map.put("PageRequest", PageRequest.class);
        log.info("[o] Configurate idClassMap in application");
        return map;
    }

    @Bean
    public DefaultPageRequestSender defaultPageRequestSender(RabbitTemplate rabbitTemplate,
                                                             LogSender logSender,
                                                             DefaultQueueDeclareService defaultQueueDeclareService,
                                                             SearchEngineProperties properties) {
        SearchEngineProperties.Common.Request request = properties
                .getCommon()
                .getRequest();
        defaultQueueDeclareService.createQueue(request.getPageRoute(), request.getExchange());
        return new DefaultPageRequestSender(rabbitTemplate, logSender, request);
    }

    @Bean
    public DefaultTempListenerContainerFactory defaultTempListenerContainerFactory(SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory,
                                                                                   DefaultQueueDeclareService defaultQueueDeclareService) {
        RabbitListenerEndpointRegistry registry = new RabbitListenerEndpointRegistry();
        return new DefaultTempListenerContainerFactory(simpleRabbitListenerContainerFactory, registry,
                                                       defaultQueueDeclareService);
    }
}
