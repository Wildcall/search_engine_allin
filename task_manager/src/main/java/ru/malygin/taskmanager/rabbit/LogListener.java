package ru.malygin.taskmanager.rabbit;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LogListener implements ApplicationListener<LogEvent> {

    Queue<LogEvent> queue = new ArrayBlockingQueue<>(100);
    RabbitTemplate rabbitTemplate;

    @Override
    public void onApplicationEvent(LogEvent logEvent) {
        queue.offer(logEvent);
    }

    @Bean
    public void tmp() {
        System.out.println("Start process");
        new Thread(() -> {
            while (true) {
                LogEvent event = queue.poll();
                if (event != null) {
                    rabbitTemplate.convertAndSend("log-exchange", event.getMessage());
                }
            }
        }).start();
    }
}
