package ru.malygin.helper.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.SimplePropertyValueConnectionNameStrategy;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.malygin.helper.MsgQueueDeclareFactory;
import ru.malygin.helper.conditions.ConditionalOnReceiverPresent;
import ru.malygin.helper.conditions.ConditionalOnSenderPresent;
import ru.malygin.helper.service.TaskReceiver;
import ru.malygin.helper.service.senders.LogSender;
import ru.malygin.helper.service.senders.NotificationSender;
import ru.malygin.helper.service.senders.TaskSender;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class MsgAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        log.info("[*] Create ObjectMapper in starter");
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
    @ConditionalOnBean(SendersConfiguration.class)
    public MsgQueueDeclareFactory msgQueueDeclareFactory(RabbitAdmin a) {
        log.info("[*] Create msgQueueDeclareFactory in starter");
        return new MsgQueueDeclareFactory(a);
    }

    @RequiredArgsConstructor
    @Configuration(proxyBeanMethods = false)
    @Conditional(ConditionalOnSenderPresent.class)
    protected static class SendersConfiguration {

        private final SearchEngineProperties seProp;

        @Bean
        @ConditionalOnProperty(prefix = "spring.search-engine.msg.log", name = "sender", havingValue = "true", matchIfMissing = true)
        @ConditionalOnMissingBean
        public LogSender logSender(RabbitTemplate r,
                                   MsgQueueDeclareFactory msgQueueDeclareFactory,
                                   ObjectMapper objectMapper) {
            msgQueueDeclareFactory.createQueue(seProp
                                                       .getMsg()
                                                       .getLog());
            LogSender logSender = new LogSender(r, objectMapper, seProp
                    .getMsg()
                    .getLog());
            log.info("[*] Create LogSender in starter");
            return logSender;
        }

        @Bean
        @ConditionalOnProperty(prefix = "spring.search-engine.msg.notification", name = "sender", havingValue = "true")
        @ConditionalOnMissingBean
        public NotificationSender notificationSender(RabbitTemplate r,
                                                     MsgQueueDeclareFactory msgQueueDeclareFactory,
                                                     ObjectMapper m) {
            Queue queue = msgQueueDeclareFactory.createQueue(seProp
                                                                     .getMsg()
                                                                     .getNotification());
            NotificationSender notificationSender = new NotificationSender(r, m, queue);
            log.info("[*] Create NotificationSender in starter");
            return notificationSender;
        }

        @Bean
        @ConditionalOnProperty(prefix = "spring.search-engine.msg.task", name = "sender", havingValue = "true")
        @ConditionalOnMissingBean
        public TaskSender taskSender(RabbitTemplate r,
                                     MsgQueueDeclareFactory msgQueueDeclareFactory,
                                     ObjectMapper m) {
            msgQueueDeclareFactory.createQueue(seProp
                                                       .getMsg()
                                                       .getCrawlerTask());
            msgQueueDeclareFactory.createQueue(seProp
                                                       .getMsg()
                                                       .getIndexerTask());
            msgQueueDeclareFactory.createQueue(seProp
                                                       .getMsg()
                                                       .getSearcherTask());
            TaskSender taskSender = new TaskSender(r, m);
            log.info("[*] Create TaskSender in starter");
            return taskSender;
        }
    }

    @RequiredArgsConstructor
    @Configuration(proxyBeanMethods = false)
    @Conditional(ConditionalOnReceiverPresent.class)
    protected static class ReceiversConfiguration {

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnProperty(prefix = "spring.search-engine.msg.task", name = "queue", havingValue = "true")
        public TaskReceiver taskReceiver(ApplicationEventPublisher p,
//                                         SearchEngineProperties seProp,
                                         ObjectMapper objectMapper,
//                                         MsgQueueDeclareFactory msgQueueDeclareFactory,
                                         Map<String, Class<?>> idClassMap) {

//            msgQueueDeclareFactory.createQueue(seProp
//                                                       .getMsg()
//                                                       .getTask());
            log.info("[*] Create TaskReceiver in starter");
            return new TaskReceiver(p, objectMapper, idClassMap);
        }
    }
}
