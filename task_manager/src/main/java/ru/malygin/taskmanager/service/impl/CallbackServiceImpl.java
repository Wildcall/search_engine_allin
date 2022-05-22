package ru.malygin.taskmanager.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.malygin.taskmanager.model.ResourceCallback;
import ru.malygin.taskmanager.model.TaskState;
import ru.malygin.taskmanager.model.entity.SiteStatus;
import ru.malygin.taskmanager.model.entity.impl.Site;
import ru.malygin.taskmanager.model.entity.impl.Task;
import ru.malygin.taskmanager.service.CallbackService;
import ru.malygin.taskmanager.service.NotificationService;
import ru.malygin.taskmanager.service.SiteService;
import ru.malygin.taskmanager.service.TaskService;

@Slf4j
@RequiredArgsConstructor
@Service
public class CallbackServiceImpl implements CallbackService {

    private final TaskService taskService;
    private final SiteService siteService;
    private final NotificationService notificationService;

    @Override
    public void process(ResourceCallback resourceCallback) {
        Task task = taskService.findById(resourceCallback.getId());
        Site site = task.getSite();
        TaskState state = TaskState.getFromState(resourceCallback.getStatus());

        //  @formatter:off
        log.info("CALLBACK RECEIVE / Resource: {} / State: {} / Task: {} / ResourceCallback: {}",
                 task.getType(),
                 state.name(),
                 task.getId(),
                 resourceCallback);
        //  @formatter:on

        task.setTaskState(state);
        task.setEndTime(resourceCallback.getEndTime());
        task.setStatId(resourceCallback.getStatId());
        taskService.update(task);

        if (state.equals(TaskState.START))
            siteService.updateStatus(site, SiteStatus.PROCESSING);
        else
            siteService.updateStatus(site, SiteStatus.READY);

        if (task.getSendNotification()) notificationService.send(task, resourceCallback);

        if (state.equals(TaskState.COMPLETE) && task.getAutoContinue()) log.info("NOT IMPL Start next task");
    }
}
