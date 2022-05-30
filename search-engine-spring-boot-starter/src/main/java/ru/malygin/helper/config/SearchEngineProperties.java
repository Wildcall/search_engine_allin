package ru.malygin.helper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("spring.search-engine")
public class SearchEngineProperties {

    private final Msg msg = new Msg();

    @Data
    public static class Msg {
        private final Queues queues = new Queues();
        private Boolean startStat = false;
        private Boolean closeStat = false;

        @Data
        public static class Queues {
            private String log = "log-queue";
            private String notification;
            private String task;
        }
    }
}
