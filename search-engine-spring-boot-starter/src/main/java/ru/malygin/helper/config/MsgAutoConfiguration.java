package ru.malygin.helper.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.malygin.helper.service.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(LogSender.class)
public class MsgAutoConfiguration {

    @RequiredArgsConstructor
    @Configuration(proxyBeanMethods = false)
    protected static class RabbitConfiguration {
        private final ObjectMapper mapper;

        @Bean
        @ConditionalOnMissingBean
        public RabbitTemplate rabbitTemplate(Jackson2JsonMessageConverter converter) {
            RabbitTemplate rabbitTemplate = new RabbitTemplate();
            rabbitTemplate.setMessageConverter(converter);
            return rabbitTemplate;
        }

        @Bean
        @ConditionalOnMissingBean
        public Jackson2JsonMessageConverter jsonConverter(DefaultClassMapper classMapper) {
            mapper.findAndRegisterModules();
            Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter(mapper);
            jsonConverter.setClassMapper(classMapper);
            return jsonConverter;
        }

        @Bean
        @ConditionalOnMissingBean
        public DefaultClassMapper classMapper(Map<String, Class<?>> idClassMap) {
            DefaultClassMapper classMapper = new DefaultClassMapper();
            classMapper.setIdClassMapping(idClassMap);
            return classMapper;
        }

        @Bean
        @ConditionalOnMissingBean
        public Map<String, Class<?>> idClassMap() {
            return new HashMap<>();
        }
    }

    @RequiredArgsConstructor
    @Configuration(proxyBeanMethods = false)
    protected static class QueueConfiguration {

        private final SearchEngineProperties properties;

        @Bean(name = "logQueue")
        @ConditionalOnMissingBean
        public Queue logQueue() {
            Queue queue = new Queue(properties
                                            .getMsg()
                                            .getQueues()
                                            .getLog(), false, false, false);
            log.info("Initialize queue: {}", queue);
            return queue;
        }

        @Bean(name = "notificationQueue")
        @ConditionalOnProperty(prefix = "spring.search-engine.msg.queues", name = "notification")
        @ConditionalOnMissingBean
        public Queue notificationQueue() {
            Queue queue = new Queue(properties
                                            .getMsg()
                                            .getQueues()
                                            .getNotification(), false, false, false);
            log.info("Initialize queue: {}", queue);
            return queue;
        }

        @Bean(name = "taskQueue")
        @ConditionalOnProperty(prefix = "spring.search-engine.msg.queues", name = "task")
        @ConditionalOnMissingBean
        public Queue taskQueue() {
            Queue queue = new Queue(properties
                                            .getMsg()
                                            .getQueues()
                                            .getTask(), false, false, false);
            log.info("Initialize queue: {}", queue);
            return queue;
        }
    }

    @RequiredArgsConstructor
    @Configuration(proxyBeanMethods = false)
    protected static class SendersConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public LogSender logSender(RabbitTemplate r,
                                   SearchEngineProperties p) {
            LogSender logSender = new LogSender(r, p
                    .getMsg()
                    .getQueues()
                    .getLog());
            log.info("Initialize sender: {}", logSender);
            return logSender;
        }

        @Bean
        @ConditionalOnBean(name = "notificationQueue")
        @ConditionalOnMissingBean
        public NotificationSender notificationSender(RabbitTemplate r,
                                                     ObjectMapper m,
                                                     SearchEngineProperties p) {
            NotificationSender notificationSender = new NotificationSender(r, m, p
                    .getMsg()
                    .getQueues()
                    .getNotification());
            log.info("Initialize sender: {}", notificationSender);
            return notificationSender;
        }

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnProperty(prefix = "spring.search-engine.msg", name = "start-stat", havingValue = "true")
        public StartingStatSender startingStatSender(LogSender logSender) {
            StartingStatSender startingStatSender = new StartingStatSender(logSender);
            log.info("Initialize sender: {}", startingStatSender);
            return startingStatSender;
        }

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnProperty(prefix = "spring.search-engine.msg", name = "close-stat", havingValue = "true")
        public ClosingStatSender closingStatSender(LogSender logSender) {
            ClosingStatSender closingStatSender = new ClosingStatSender(logSender);
            log.info("Initialize sender: {}", closingStatSender);
            return closingStatSender;
        }

    }

    @RequiredArgsConstructor
    @Configuration(proxyBeanMethods = false)
    protected static class TaskReceiverConfiguration<T> {

        private final SearchEngineProperties properties;

        @Bean
        @ConditionalOnProperty(prefix = "spring.search-engine.msg.queues", name = "task")
        @ConditionalOnMissingBean
        public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                                        TaskReceiver<T> receiver) {
            SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
            container.setConnectionFactory(connectionFactory);
            container.setQueueNames(properties
                                            .getMsg()
                                            .getQueues()
                                            .getTask());
            container.setMessageListener(receiver);
            return container;
        }

        @Bean
        @ConditionalOnProperty(prefix = "spring.search-engine.msg.queues", name = "task")
        @ConditionalOnMissingBean
        public TaskReceiver<T> taskReceiver(Jackson2JsonMessageConverter c,
                                            ApplicationEventPublisher p) {
            return new TaskReceiver<>(c, p);
        }
    }
}
