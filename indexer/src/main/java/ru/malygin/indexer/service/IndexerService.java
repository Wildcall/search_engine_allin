package ru.malygin.indexer.service;

import org.springframework.context.event.EventListener;
import ru.malygin.helper.model.TaskReceiveEvent;

public interface IndexerService {

    @EventListener(TaskReceiveEvent.class)
    void process(TaskReceiveEvent event);
}
