package ru.malygin.helper.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.malygin.helper.QueueDeclareFactory;
import ru.malygin.helper.service.senders.LogSender;
import ru.malygin.helper.service.senders.MetricsSender;
import ru.malygin.helper.service.senders.NotificationSender;
import ru.malygin.helper.service.senders.impl.DefaultLogSender;
import ru.malygin.helper.service.senders.impl.DefaultMetricsSender;
import ru.malygin.helper.service.senders.impl.DefaultNotificationSender;

import static ru.malygin.helper.config.SearchEngineProperties.Common.Log;

@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class CommonSendersConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "spring.search-engine.common.log", name = "sender", havingValue = "true")
    @ConditionalOnMissingBean
    public LogSender logSender(RabbitTemplate r,
                               QueueDeclareFactory queueDeclareFactory,
                               ObjectMapper objectMapper,
                               SearchEngineProperties properties) {
        Log logProperties = properties
                .getCommon()
                .getLog();
        queueDeclareFactory.createQueue(logProperties.getErrorRoute(), logProperties.getExchange());
        queueDeclareFactory.createQueue(logProperties.getInfoRoute(), logProperties.getExchange());
        DefaultLogSender defaultLogSender = new DefaultLogSender(r, objectMapper, logProperties);
        log.info("[*] Create DefaultLogSender in starter");
        return defaultLogSender;
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.search-engine.common.notification", name = "sender", havingValue = "true")
    @ConditionalOnMissingBean
    public NotificationSender notificationSender(RabbitTemplate r,
                                                 QueueDeclareFactory queueDeclareFactory,
                                                 ObjectMapper m,
                                                 SearchEngineProperties properties) {
        SearchEngineProperties.Common.Notification notification = properties
                .getCommon()
                .getNotification();
        queueDeclareFactory.createQueue(notification.getNotificationRoute(), notification.getExchange());
        DefaultNotificationSender notificationSender = new DefaultNotificationSender(r, m, notification);
        log.info("[*] Create DefaultNotificationSender in starter");
        return notificationSender;
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.search-engine.common.metrics", name = "sender", havingValue = "true")
    @ConditionalOnMissingBean
    public MetricsSender metricsSender(QueueDeclareFactory queueDeclareFactory,
                                       SearchEngineProperties properties) {
        SearchEngineProperties.Common.Metrics metrics = properties
                .getCommon()
                .getMetrics();
        queueDeclareFactory.createQueue(metrics.getMetricsRoute(), metrics.getExchange());
        DefaultMetricsSender defaultMetricsSender = new DefaultMetricsSender();
        log.info("[*] Create DefaultMetricsSender in starter");
        return defaultMetricsSender;
    }
//
//    @Bean
//    @ConditionalOnProperty(prefix = "spring.search-engine.msg.task", name = "sender", havingValue = "true")
//    @ConditionalOnMissingBean
//    public TaskSender taskSender(RabbitTemplate r,
//                                 MsgQueueDeclareFactory msgQueueDeclareFactory,
//                                 ObjectMapper m,
//                                 SearchEngineProperties seProp) {
//        msgQueueDeclareFactory.createQueue(seProp
//                                                   .getMsg()
//                                                   .getCrawlerTask());
//        msgQueueDeclareFactory.createQueue(seProp
//                                                   .getMsg()
//                                                   .getIndexerTask());
//        msgQueueDeclareFactory.createQueue(seProp
//                                                   .getMsg()
//                                                   .getSearcherTask());
//        TaskSender taskSender = new TaskSender(r, m);
//        log.info("[*] Create TaskSender in starter");
//        return taskSender;
//    }
}
