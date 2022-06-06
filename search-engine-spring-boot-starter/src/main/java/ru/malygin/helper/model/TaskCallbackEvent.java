package ru.malygin.helper.model;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TaskCallbackEvent extends ApplicationEvent {

    private final TaskCallback callback;

    public TaskCallbackEvent(TaskCallback callback) {
        super(callback);
        this.callback = callback;
    }
}
