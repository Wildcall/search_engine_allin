package ru.malygin.taskmanager.rabbit;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LogPublisher {

    ApplicationEventPublisher publisher;

    public LogPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(final String message) {
        LogEvent logEvent = new LogEvent(message);
        publisher.publishEvent(logEvent);
    }
}
