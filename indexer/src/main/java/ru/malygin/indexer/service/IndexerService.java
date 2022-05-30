package ru.malygin.indexer.service;

import ru.malygin.indexer.model.Task;
import ru.malygin.indexer.model.TaskAction;

public interface IndexerService {
    void process(Task task,
                 TaskAction action);
}
