package ru.malygin.crawler.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.malygin.crawler.model.Task;
import ru.malygin.helper.config.SearchEngineProperties;
import ru.malygin.helper.model.PageRequest;
import ru.malygin.helper.service.DefaultQueueDeclareService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class AppConfig {

    @Bean
    protected Map<String, Class<?>> idClassMap() {
        Map<String, Class<?>> map = new HashMap<>();
        map.put("Task", Task.class);
        map.put("PageRequest", PageRequest.class);
        log.info("[o] Configurate idClassMap in application");
        return map;
    }

    @Bean
    public boolean declareQueue(DefaultQueueDeclareService defaultQueueDeclareService,
                                SearchEngineProperties properties) {
        SearchEngineProperties.Common.Request request = properties
                .getCommon()
                .getRequest();
        defaultQueueDeclareService.createQueue(request.getPageRoute(), request.getExchange());
        return true;
    }
}
