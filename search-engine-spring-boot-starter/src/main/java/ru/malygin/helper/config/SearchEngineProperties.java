package ru.malygin.helper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component("properties")
@ConfigurationProperties("spring.search-engine")
public class SearchEngineProperties {

    private final Common common = new Common();

    @Data
    public static class Common {
        private final Log log = new Log();
        private final Notification notification = new Notification();
        private final Metrics metrics = new Metrics();
        private final Task task = new Task();
        private final Callback callback = new Callback();

        @Data
        public static class Log {
            private Boolean sender = false;
            private Boolean receiver = false;
            private String exchange = "log-exchange";
            private String errorRoute = "error";
            private String infoRoute = "info";
        }

        @Data
        public static class Notification {
            private Boolean sender = false;
            private Boolean receiver = false;
            private String exchange = "notification-exchange";
            private String notificationRoute = "notification";
        }

        @Data
        public static class Metrics {
            private Boolean sender = false;
            private Boolean receiver = false;
            private String exchange = "metrics-exchange";
            private String metricsRoute = "metrics";
        }

        @Data
        public static class Task {
            private Boolean receiver = false;
            private String exchange = "task";
            private String route = "default-task-queue";
        }

        @Data
        public static class Callback {
            private Boolean sender = true;
            private String exchange = "task-callback";
            private String route = "task-callback-queue";
        }
    }
}
