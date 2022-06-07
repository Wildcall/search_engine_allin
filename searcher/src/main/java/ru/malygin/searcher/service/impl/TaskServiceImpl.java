package ru.malygin.searcher.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.malygin.helper.enums.TaskAction;
import ru.malygin.helper.events.TaskReceiveEvent;
import ru.malygin.helper.service.TaskService;
import ru.malygin.helper.service.senders.LogSender;
import ru.malygin.searcher.model.Task;
import ru.malygin.searcher.service.SearcherService;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final LogSender logSender;
    private final SearcherService searcherService;

    @Override
    public void process(TaskReceiveEvent event) {
        TaskAction action = event.getAction();
        Task task = (Task) event.getTask();
        logSender.info("TASK RECEIVE / Id: %s / Action: %s", task.getId(), action.name());
        if (action.equals(TaskAction.START)) {
            searcherService.start(task);
        }
        if (action.equals(TaskAction.STOP)) {
            searcherService.stop(task);
        }
    }

}
