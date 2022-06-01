package ru.malygin.helper.model;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

@Getter
public class LogReceiveEvent extends ApplicationEvent {
    private final Map<String, String> map;
    private final String type;

    public LogReceiveEvent(Map<String, String> map,
                           String type) {
        super(map);
        this.map = map;
        this.type = type;
    }
}
