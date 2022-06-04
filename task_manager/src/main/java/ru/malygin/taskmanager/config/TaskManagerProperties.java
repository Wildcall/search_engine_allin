package ru.malygin.taskmanager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component("taskManagerProperties")
@ConfigurationProperties("spring.search-engine.task-manager")
public class TaskManagerProperties {
    private String secret = "amtzZGJuZmFzaWx1ZGRmO2xrbnFsO2tmanF3ay9kZm13O2xma3cnO2RmLHF3LmRmbXdkZgo";
    private Crawler crawler = new Crawler();
    private Indexer indexer = new Indexer();
    private Searcher searcher = new Searcher();


    public interface ServiceQueueProperties {
        String getExchange();

        String getRoute();
    }

    @Data
    public static class Crawler implements ServiceQueueProperties {
        private String exchange = "task";
        private String route = "crawler-task-queue";
    }

    @Data
    public static class Indexer implements ServiceQueueProperties {
        private String exchange = "task";
        private String route = "indexer-task-queue";
    }

    @Data
    public static class Searcher implements ServiceQueueProperties {
        private String exchange = "task";
        private String route = "searcher-task-queue";
    }
}
