package ru.malygin.taskmanager.rabbit.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.malygin.taskmanager.config.RabbitMQConfig;
import ru.malygin.taskmanager.rabbit.Sender;

@RequiredArgsConstructor
@Service
public class LogSender implements Sender, CommandLineRunner {

    private final RabbitMQConfig rabbitMQConfig;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void send(Object o) {
        if (o != null)
            rabbitTemplate.convertAndSend(rabbitMQConfig.getLogQueueName(), o);
    }

    @Override
    public void run(String... args) {
        this.send("Init task manager");
    }
}
