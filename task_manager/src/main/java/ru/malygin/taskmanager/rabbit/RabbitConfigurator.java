package ru.malygin.taskmanager.rabbit;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RabbitConfigurator {

    @Bean
    public Queue queue() {
        return new Queue("log-queue", false, false, false);
    }

//    @Bean
//    public DirectExchange exchange() {
//        return new DirectExchange("log-exchange", false, false);
//    }
}
