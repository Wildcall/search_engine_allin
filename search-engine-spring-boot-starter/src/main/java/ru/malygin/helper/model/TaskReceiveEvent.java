package ru.malygin.helper.model;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TaskReceiveEvent extends ApplicationEvent {

    private final Object task;
    private final TaskAction action;

    public TaskReceiveEvent(Object task,
                            TaskAction a) {
        super(task);
        this.task = task;
        this.action = a;
    }
}
