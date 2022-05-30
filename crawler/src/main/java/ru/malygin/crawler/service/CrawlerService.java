package ru.malygin.crawler.service;

import org.springframework.context.event.EventListener;
import ru.malygin.crawler.model.Task;
import ru.malygin.helper.model.TaskReceiveEvent;

public interface CrawlerService {

    @EventListener(TaskReceiveEvent.class)
    void process(TaskReceiveEvent<Task> t);
}
