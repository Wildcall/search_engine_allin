package ru.malygin.taskmanager.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.malygin.helper.model.TaskCallback;
import ru.malygin.helper.enums.TaskState;
import ru.malygin.helper.service.senders.LogSender;
import ru.malygin.taskmanager.model.SiteStatus;
import ru.malygin.taskmanager.model.entity.impl.Site;
import ru.malygin.taskmanager.model.entity.impl.Task;
import ru.malygin.taskmanager.service.SiteService;
import ru.malygin.taskmanager.service.TaskService;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskCallbackReceiverServiceImpl implements ru.malygin.taskmanager.service.TaskCallbackReceiverService {

    static {
        log.info("[o] Create TaskCallbackReceiverService in application");
    }

    private final LogSender logSender;
    private final TaskService taskService;
    private final SiteService siteService;

    @Override
    public void receiveTaskCallback(TaskCallback taskCallback) {
        logSender.info("TASK CALLBACK RECEIVE / Id: %s / State: %s", taskCallback.getTaskId(), taskCallback.getState());
        Task task = taskService.findById(taskCallback.getTaskId());
        Site site = task.getSite();
        TaskState state = taskCallback.getState();

        task.setTaskState(state);
        task.setStartTime(taskCallback.getStartTime());
        task.setEndTime(taskCallback.getEndTime());
        taskService.update(task);

        if (!state.equals(TaskState.START)) siteService.updateStatus(site, SiteStatus.READY);

        //notification

        //auto next task
    }
}
