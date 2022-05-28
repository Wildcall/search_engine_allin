package ru.malygin.crawler.service;

import ru.malygin.crawler.model.Task;
import ru.malygin.crawler.model.TaskAction;

public interface CrawlerService {

    void process(Task task,
                 TaskAction action);
}
