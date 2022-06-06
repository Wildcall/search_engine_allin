package ru.malygin.indexer.service;

import ru.malygin.indexer.model.Task;

public interface IndexerService {

    void start(Task task);

    void stop(Task task);
}
