package ru.malygin.taskmanager.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.malygin.taskmanager.exception.BadRequestException;
import ru.malygin.taskmanager.exception.ResourceBadRequestException;
import ru.malygin.taskmanager.facade.ResourceFacade;
import ru.malygin.taskmanager.model.TaskState;
import ru.malygin.taskmanager.model.entity.SiteStatus;
import ru.malygin.taskmanager.model.entity.impl.AppUser;
import ru.malygin.taskmanager.model.entity.impl.Site;
import ru.malygin.taskmanager.model.entity.impl.Task;
import ru.malygin.taskmanager.service.AppUserService;
import ru.malygin.taskmanager.service.ResourceService;
import ru.malygin.taskmanager.service.TaskService;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class ResourceFacadeImpl implements ResourceFacade {

    private final AppUserService appUserService;
    private final TaskService taskService;
    private final ResourceService resourceService;

    @Override
    public Map<String, Long> start(Authentication authentication,
                                   Long id) {
        //  @formatter:off
        AppUser appUser = appUserService.findByAuthentication(authentication);
        Task task = taskService.findByAppUserAndId(appUser, id);
        Site site = task.getSite();

        TaskState taskState = task.getTaskState();
        SiteStatus siteStatus = site.getStatus();
        int order = task.getType().getOrder();

        // Any task is already starting for the site
        if (siteStatus.equals(SiteStatus.PROCESSING))
            throw new BadRequestException("You cannot start a task, a task is already running for the site");

        // If the task is starting
        if (taskState.equals(TaskState.START))
            throw new BadRequestException("You cannot start a task that is already starting");

        // The previous task is not successfully completed
        List<Task> siteTasks = taskService.findAllByAppUserAndSite(appUser, site);
        if (siteTasks.stream().noneMatch(t -> order == 0
                                             || (t.getType().getOrder() == order - 1
                                             && t.getTaskState().equals(TaskState.COMPLETE))))
            throw new BadRequestException("You cannot start a task for which a previous task has not been finished");

        // Resource for the task is not available
        if (resourceService.resourceNotAvailable(task.getType()))
            throw new BadRequestException("Resource for the task is not available, try again later");

        // Resource responded with an error
        if (!resourceService.start(task))
            throw new ResourceBadRequestException("The task resource responded with an error");

        taskService.resetTask(task);
        appUserService.updateLastActionTime(appUser);
        return Map.of("siteId", site.getId(),
                      "appUserId", appUser.getId());
        //  @formatter:on
    }

    @Override
    public String stop(Authentication authentication,
                       Long id) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        Task task = taskService.findByAppUserAndId(appUser, id);
        Site site = task.getSite();

        TaskState taskState = task.getTaskState();

        // If the task is starting
        if (!taskState.equals(TaskState.START)) throw new BadRequestException(
                "You cannot stop a task that has not yet started");

        // Resource for the task is not available
        if (resourceService.resourceNotAvailable(task.getType())) throw new BadRequestException(
                "Resource for the task is not available, try again later");

        // Resource responded with an error
        if (!resourceService.stop(task)) throw new ResourceBadRequestException(
                "The task resource responded with an error");

        appUserService.updateLastActionTime(appUser);
        return "Task successfully interrupted";
    }
}
