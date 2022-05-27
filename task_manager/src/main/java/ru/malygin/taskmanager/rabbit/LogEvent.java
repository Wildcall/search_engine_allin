package ru.malygin.taskmanager.rabbit;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LogEvent extends ApplicationEvent {

    String message;

    public LogEvent(String message) {
        super(message);
        this.message = message;
    }
}
