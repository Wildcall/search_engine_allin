package ru.malygin.helper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component("seProp")
@ConfigurationProperties("spring.search-engine")
public class SearchEngineProperties {

    private final Msg msg = new Msg();

    @Data
    public static class Msg {
        private final Log log = new Log();
        private final Notification notification = new Notification();
        private final Task task = new Task();
        private final CrawlerTask crawlerTask = new CrawlerTask();
        private final IndexerTask indexerTask = new IndexerTask();
        private final SearcherTask searcherTask = new SearcherTask();

        public interface BaseMsg {
            String getQueue();

            String getExchange();
        }

        @Data
        public static class Log implements BaseMsg {
            private Boolean sender = true;
            private String queue = "log-queue";
            private String exchange;
        }

        @Data
        public static class Notification implements BaseMsg {
            private Boolean sender = false;
            private String queue = "notification-queue";
            private String exchange;
        }

        @Data
        public static class Task implements BaseMsg {
            private String queue = "default-task-queue";
            private String exchange;
        }

        @Data
        public static class CrawlerTask implements BaseMsg {
            private String queue = "crawler-task-queue";
            private String exchange;
        }

        @Data
        public static class IndexerTask implements BaseMsg {
            private String queue = "indexer-task-queue";
            private String exchange;
        }

        @Data
        public static class SearcherTask implements BaseMsg {
            private String queue = "searcher-task-queue";
            private String exchange;
        }
    }
}
