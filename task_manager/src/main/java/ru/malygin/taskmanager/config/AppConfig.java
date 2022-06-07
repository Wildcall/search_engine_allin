package ru.malygin.taskmanager.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.malygin.helper.config.SearchEngineProperties;
import ru.malygin.helper.model.TaskCallback;
import ru.malygin.helper.service.QueueDeclareService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class AppConfig {

    @Bean
    public boolean declareQueue(TaskManagerProperties taskProperties,
                                QueueDeclareService queueDeclareService,
                                SearchEngineProperties properties) {
        TaskManagerProperties.Crawler crawler = taskProperties.getCrawler();
        TaskManagerProperties.Indexer indexer = taskProperties.getIndexer();
        TaskManagerProperties.Searcher searcher = taskProperties.getSearcher();
        SearchEngineProperties.Common.Callback callback = properties
                .getCommon()
                .getCallback();
        queueDeclareService.createQueue(callback.getRoute(), callback.getExchange());
        queueDeclareService.createQueue(crawler.getRoute(), crawler.getExchange());
        queueDeclareService.createQueue(indexer.getRoute(), indexer.getExchange());
        queueDeclareService.createQueue(searcher.getRoute(), searcher.getExchange());
        return true;
    }

    @Bean
    protected Map<String, Class<?>> idClassMap() {
        Map<String, Class<?>> map = new HashMap<>();
        map.put("TaskCallback", TaskCallback.class);
        log.info("[o] Configurate idClassMap in application");
        return map;
    }
}
