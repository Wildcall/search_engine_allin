package ru.malygin.helper.model;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class LogReceiveEvent extends ApplicationEvent {
    private final String message;
    private final String type;

    public LogReceiveEvent(String message,
                           String type) {
        super(message);
        this.message = message;
        this.type = type;
    }
}
