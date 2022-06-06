package ru.malygin.taskmanager.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.malygin.helper.model.TaskCallback;
import ru.malygin.helper.service.QueueDeclareService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class AppConfig {

    @Bean
    public boolean declareQueue(TaskManagerProperties properties,
                                QueueDeclareService queueDeclareService) {
        TaskManagerProperties.Crawler crawler = properties.getCrawler();
        TaskManagerProperties.Indexer indexer = properties.getIndexer();
        TaskManagerProperties.Searcher searcher = properties.getSearcher();
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
