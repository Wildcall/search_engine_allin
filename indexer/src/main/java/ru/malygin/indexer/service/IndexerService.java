package ru.malygin.indexer.service;

import org.springframework.context.event.EventListener;
import ru.malygin.helper.model.TaskReceiveEvent;
import ru.malygin.indexer.model.Task;

public interface IndexerService {

    @EventListener(TaskReceiveEvent.class)
    void process(TaskReceiveEvent<Task> event);
}
