package ru.malygin.crawler.rabbit.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class LogSender implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    @Value(value = "${spring.application.name}")
    private String appName;
    @Value(value = "${rabbit.queues.log-queue}")
    private String logQueueName;

    @Bean
    public Queue getLogQueue() {
        return new Queue(logQueueName, false, false, false);
    }

    public void send(String s,
                     Object... o) {
        if (o != null && s != null) {
            String msg = String.format(s, o);
            String log = String.format("Application: %s / Time: %s / Message: {%s}", appName, LocalDateTime.now(), msg);
            rabbitTemplate.convertAndSend(logQueueName, log);
        }
    }

    @Override
    public void run(String... args) {
        this.send("Init crawler");
    }
}
