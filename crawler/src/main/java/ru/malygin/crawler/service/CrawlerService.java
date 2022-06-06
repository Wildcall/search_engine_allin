package ru.malygin.crawler.service;

import ru.malygin.crawler.model.Task;

public interface CrawlerService {

    void start(Task task);

    void stop(Task task);
}
