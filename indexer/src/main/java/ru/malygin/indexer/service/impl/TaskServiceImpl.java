package ru.malygin.indexer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.malygin.helper.model.TaskAction;
import ru.malygin.helper.model.TaskReceiveEvent;
import ru.malygin.helper.service.senders.LogSender;
import ru.malygin.indexer.model.Task;
import ru.malygin.indexer.service.IndexerService;
import ru.malygin.indexer.service.TaskService;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final LogSender logSender;
    private final IndexerService indexerService;

    @Override
    public void process(TaskReceiveEvent event) {
        TaskAction action = event.getAction();
        Task task = (Task) event.getTask();
        logSender.info("TASK RECEIVE / Id: %s / Action: %s", task.getId(), action.name());
        if (action.equals(TaskAction.START)) {
            indexerService.start(task);
        }
        if (action.equals(TaskAction.STOP)) {
            indexerService.stop(task);
        }
    }

}
