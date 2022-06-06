package ru.malygin.crawler.service;

import org.springframework.context.event.EventListener;
import ru.malygin.helper.events.TaskReceiveEvent;

public interface TaskService {

    @EventListener(TaskReceiveEvent.class)
    void process(TaskReceiveEvent event);
}
