package ru.malygin.helper.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.malygin.helper.model.TaskCallback;

@Getter
public class TaskCallbackEvent extends ApplicationEvent {

    private final TaskCallback callback;

    public TaskCallbackEvent(TaskCallback callback) {
        super(callback);
        this.callback = callback;
    }
}
