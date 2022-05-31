package ru.malygin.helper.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.SimplePropertyValueConnectionNameStrategy;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.malygin.helper.MsgQueueDeclareFactory;
import ru.malygin.helper.MsgReceiverFactory;
import ru.malygin.helper.service.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(LogSender.class)
public class MsgAutoConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper;
    }

    @Bean
    public boolean customizeRabbitTemplate(RabbitTemplate rabbitTemplate,
                                           Jackson2JsonMessageConverter converter) {
        log.info("[*] Customize RabbitTemplate in starter");
        rabbitTemplate.setMessageConverter(converter);
        return false;
    }

    @Bean
    public SimplePropertyValueConnectionNameStrategy simplePropertyValueConnectionNameStrategy() {
        log.info("[*] Change connection name in starter");
        return new SimplePropertyValueConnectionNameStrategy("spring.application.name");
    }

    @Bean
    @Primary
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        log.info("[*] Create RabbitAdmin in starter");
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    @Primary
    public RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry() {
        log.info("[*] Create RabbitListenerEndpointRegistry in starter");
        return new RabbitListenerEndpointRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    public Jackson2JsonMessageConverter jsonConverter(DefaultClassMapper classMapper,
                                                      ObjectMapper mapper) {
        log.info("[*] Create Jackson2JsonMessageConverter in starter");
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter(mapper);
        jsonConverter.setClassMapper(classMapper);
        return jsonConverter;
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultClassMapper classMapper(Map<String, Class<?>> idClassMap) {
        log.info("[*] Create DefaultClassMapper in starter");
        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setIdClassMapping(idClassMap);
        return classMapper;
    }

    @Bean
    @ConditionalOnMissingBean
    public Map<String, Class<?>> idClassMap() {
        log.info("[*] Create DefaultIdClassMap in starter");
        return new HashMap<>();
    }

    @Bean
    @ConditionalOnMissingBean
    public MsgQueueDeclareFactory msgQueueDeclareFactory(RabbitAdmin a) {
        log.info("[*] Create msgQueueDeclareFactory in starter");
        return new MsgQueueDeclareFactory(a);
    }

    @Bean
    @ConditionalOnMissingBean
    public MsgReceiverFactory msgReceiverFactory(SimpleRabbitListenerContainerFactory f,
                                                 RabbitListenerEndpointRegistry r) {
        log.info("[*] Create MsgReceiverFactory in starter");
        return new MsgReceiverFactory(f, r);
    }

    @RequiredArgsConstructor
    @Configuration(proxyBeanMethods = false)
    protected static class SendersConfiguration {

        private final MsgQueueDeclareFactory msgQueueDeclareFactory;
        private final SearchEngineProperties properties;

        @Bean
        public LogSender logSender(RabbitTemplate r) {
            Queue queue = msgQueueDeclareFactory.createQueue(properties
                                                                     .getMsg()
                                                                     .getLog());
            LogSender logSender = new LogSender(r, queue);
            log.info("[*] Create LogSender in starter");
            return logSender;
        }

        @Bean
        @ConditionalOnProperty(prefix = "spring.search-engine.msg", name = "notification.sender", havingValue = "true")
        @ConditionalOnMissingBean
        public NotificationSender notificationSender(RabbitTemplate r,
                                                     ObjectMapper m) {
            Queue queue = msgQueueDeclareFactory.createQueue(properties
                                                                     .getMsg()
                                                                     .getNotification());
            NotificationSender notificationSender = new NotificationSender(r, m, queue);
            log.info("[*] Create NotificationSender in starter");
            return notificationSender;
        }

        @Bean
        @ConditionalOnProperty(prefix = "spring.search-engine.msg", name = {"crawler-task.queue", "indexer-task.queue", "searcher-task.queue"})
        @ConditionalOnMissingBean
        public TaskSender taskSender(RabbitTemplate r,
                                     ObjectMapper m) {
            msgQueueDeclareFactory.createQueue(properties
                                                       .getMsg()
                                                       .getCrawlerTask());
            msgQueueDeclareFactory.createQueue(properties
                                                       .getMsg()
                                                       .getIndexerTask());
            msgQueueDeclareFactory.createQueue(properties
                                                       .getMsg()
                                                       .getSearcherTask());
            TaskSender taskSender = new TaskSender(r, m);
            log.info("[*] Create TaskSender in starter");
            return taskSender;
        }

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnProperty(prefix = "spring.search-engine.msg", name = "log.start-stat", havingValue = "true")
        public StartingStatSender startingStatSender(LogSender logSender) {
            StartingStatSender startingStatSender = new StartingStatSender(logSender);
            log.info("[*] Create StartingStatSender in starter");
            return startingStatSender;
        }

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnProperty(prefix = "spring.search-engine.msg", name = "log.close-stat", havingValue = "true")
        public ClosingStatSender closingStatSender(LogSender logSender) {
            ClosingStatSender closingStatSender = new ClosingStatSender(logSender);
            log.info("[*] Create ClosingStatSender in starter");
            return closingStatSender;
        }

    }

    @RequiredArgsConstructor
    @Configuration(proxyBeanMethods = false)
    protected static class ReceiversConfiguration {

        private final MsgQueueDeclareFactory msgQueueDeclareFactory;
        private final SearchEngineProperties properties;
        private final MsgReceiverFactory msgReceiverFactory;

        @Bean(name = "taskReceiver")
        @ConditionalOnProperty(prefix = "spring.search-engine.msg.task", name = "queue")
        public <T> MessageListener taskReceiver(Jackson2JsonMessageConverter c,
                                                ApplicationEventPublisher p) {
            Queue queue = msgQueueDeclareFactory.createQueue(properties
                                                                     .getMsg()
                                                                     .getTask());
            log.info("[*] Create TaskReceiver in starter");
            TaskReceiver<T> receiver = new TaskReceiver<>(c, p);
            return msgReceiverFactory.create(queue, receiver);
        }

        @Bean(name = "logReceiver")
        @ConditionalOnProperty(prefix = "spring.search-engine.msg.log", name = "receiver", havingValue = "true")
        public MessageListener logReceiver(ApplicationEventPublisher p) {
            Queue queue = msgQueueDeclareFactory.createQueue(properties
                                                                     .getMsg()
                                                                     .getLog());
            log.info("[*] Create LogReceiver in starter");
            LogReceiver receiver = new LogReceiver(p);
            return msgReceiverFactory.create(queue, receiver);
        }
    }
}
