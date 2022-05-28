package ru.malygin.taskmanager.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@RequiredArgsConstructor
@Configuration
public class RabbitMQConfig {

    private final String logQueueName = "log-queue";
    private final String taskQueueName = "crawler-task-queue";

    @Bean
    public Queue getLogQueue() {
        return new Queue(logQueueName, false, false, false);
    }

    @Bean
    public Queue getTaskQueue() {
        return new Queue(taskQueueName, false, false, false);
    }
}
