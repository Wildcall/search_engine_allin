package ru.malygin.crawler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.malygin.crawler.model.Task;
import ru.malygin.crawler.service.CrawlerService;
import ru.malygin.crawler.service.TaskService;
import ru.malygin.helper.enums.TaskAction;
import ru.malygin.helper.events.TaskReceiveEvent;
import ru.malygin.helper.service.senders.LogSender;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final LogSender logSender;
    private final CrawlerService crawlerService;

    @Override
    public void process(TaskReceiveEvent event) {
        TaskAction action = event.getAction();
        Task task = (Task) event.getTask();
        logSender.info("TASK RECEIVE / Id: %s / Action: %s", task.getId(), action.name());
        if (action.equals(TaskAction.START)) {
            crawlerService.start(task);
        }
        if (action.equals(TaskAction.STOP)) {
            crawlerService.stop(task);
        }
    }

}
