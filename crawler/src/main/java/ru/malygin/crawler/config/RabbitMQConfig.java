package ru.malygin.crawler.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@RequiredArgsConstructor
@Configuration
public class RabbitMQConfig {

    private final String logQueueName = "log-queue";
    private final RabbitTemplate rabbitTemplate;

    @Bean
    public Queue getLogQueue() {
        return new Queue(logQueueName, false, false, false);
    }

    @Bean
    public void configureRabbitTemplate() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter(mapper));
    }
}
