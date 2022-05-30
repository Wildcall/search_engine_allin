package ru.malygin.helper.model;

import org.springframework.context.ApplicationEvent;

public class TaskReceiveEvent<T> extends ApplicationEvent {

    private final T t;
    private final TaskAction action;

    public TaskReceiveEvent(T t,
                            TaskAction a) {
        super(t);
        this.t = t;
        this.action = a;
    }

    public T getTask() {
        return t;
    }

    public TaskAction getAction() {
        return action;
    }
}
