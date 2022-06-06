package ru.malygin.helper.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.malygin.helper.service.DefaultQueueDeclareService;
import ru.malygin.helper.service.receivers.TaskReceiver;
import ru.malygin.helper.service.receivers.impl.DefaultTaskReceiver;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class CommonReceiversConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "spring.search-engine.common.task", name = "receiver", havingValue = "true")
    @ConditionalOnMissingBean
    public TaskReceiver taskReceiver(ApplicationEventPublisher p,
                                     ObjectMapper objectMapper,
                                     DefaultQueueDeclareService defaultQueueDeclareService,
                                     SearchEngineProperties properties,
                                     Map<String, Class<?>> idClassMap) {
        SearchEngineProperties.Common.Task taskProp = properties
                .getCommon()
                .getTask();
        defaultQueueDeclareService.createQueue(taskProp.getRoute(), taskProp.getExchange());
        log.info("[*] Create DefaultTaskReceiver in starter");
        return new DefaultTaskReceiver(p, objectMapper, idClassMap);
    }
}
