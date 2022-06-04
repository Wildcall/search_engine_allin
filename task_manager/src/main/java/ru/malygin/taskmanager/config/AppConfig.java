package ru.malygin.taskmanager.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.malygin.helper.service.QueueDeclareService;

@Getter
@RequiredArgsConstructor
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
}
