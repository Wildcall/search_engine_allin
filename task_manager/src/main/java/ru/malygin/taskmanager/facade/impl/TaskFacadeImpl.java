package ru.malygin.taskmanager.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.malygin.helper.enums.ServiceType;
import ru.malygin.helper.enums.TaskAction;
import ru.malygin.helper.enums.TaskState;
import ru.malygin.helper.service.senders.LogSender;
import ru.malygin.taskmanager.config.TaskManagerProperties;
import ru.malygin.taskmanager.exception.BadRequestException;
import ru.malygin.taskmanager.facade.TaskFacade;
import ru.malygin.taskmanager.model.SiteStatus;
import ru.malygin.taskmanager.model.dto.BaseDto;
import ru.malygin.taskmanager.model.dto.impl.TaskDto;
import ru.malygin.taskmanager.model.entity.impl.AppUser;
import ru.malygin.taskmanager.model.entity.impl.Setting;
import ru.malygin.taskmanager.model.entity.impl.Site;
import ru.malygin.taskmanager.model.entity.impl.Task;
import ru.malygin.taskmanager.service.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskFacadeImpl implements TaskFacade {

    private final AppUserService appUserService;
    private final TaskService taskService;
    private final SiteService siteService;
    private final SettingsService settingsService;
    private final TaskSenderService taskSenderService;
    private final TaskManagerProperties properties;
    private final LogSender logSender;

    @Override
    public BaseDto save(Authentication authentication,
                        TaskDto taskDto) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        Site site = siteService.findById(appUser, taskDto.getSiteId());
        Setting setting = settingsService.findById(appUser, taskDto.getSettingId());
        ServiceType serviceType = setting.getType();

        Task task = (Task) taskDto.toBaseEntity();
        task.setAppUser(appUser);
        task.setSite(site);
        task.setSetting(setting);
        task.setType(serviceType);
        task.setTaskState(TaskState.CREATE);

        taskService.save(task);
        appUserService.updateLastActionTime(appUser);
        return task.toBaseDto();
    }

    @Override
    public BaseDto update(Authentication authentication,
                          TaskDto taskDto) {
        AppUser appUser = appUserService.findByAuthentication(authentication);

        Task existTask = taskService.findByAppUserAndId(appUser, taskDto.getId());

        if (existTask
                .getTaskState()
                .equals(TaskState.START)) throw new BadRequestException(
                "You cannot update a task that is already started");

        existTask.setSendNotification(taskDto.getSendNotification());
        existTask.setAutoContinue(taskDto.getAutoContinue());
        existTask.setEventFreqInMs(taskDto.getEventFreqInMs());

        taskService.update(existTask);
        appUserService.updateLastActionTime(appUser);
        return existTask.toBaseDto();
    }

    @Override
    public BaseDto findById(Authentication authentication,
                            Long id) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        return taskService
                .findByAppUserAndId(appUser, id)
                .toBaseDto();
    }

    @Override
    public List<BaseDto> findAll(Authentication authentication) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        return taskService
                .findAllByAppUser(appUser)
                .stream()
                .map(Task::toBaseDto)
                .toList();
    }

    @Override
    public List<BaseDto> findAllByResourceStringType(Authentication authentication,
                                                     String type) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        ServiceType serviceType = convertStringTypeToResourceType(type);
        return taskService
                .findAllByAppUserAndResourceType(appUser, serviceType)
                .stream()
                .map(Task::toBaseDto)
                .toList();
    }

    @Override
    public List<BaseDto> findAllBySiteId(Authentication authentication,
                                         Long siteId) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        Site site = siteService.findById(appUser, siteId);
        return taskService
                .findAllByAppUserAndSite(appUser, site)
                .stream()
                .map(Task::toBaseDto)
                .toList();
    }

    @Override
    public List<BaseDto> findAllByTaskStateString(Authentication authentication,
                                                  String state) {
        try {
            TaskState taskState = TaskState.valueOf(state.toUpperCase(Locale.ROOT));
            AppUser appUser = appUserService.findByAuthentication(authentication);

            return taskService
                    .findAllByAppUserAndTaskState(appUser, taskState)
                    .stream()
                    .map(Task::toBaseDto)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(
                    "Type of task status: " + state + " is invalid. Use one of " + Arrays.toString(TaskState.values()));
        }
    }

    @Override
    public Long deleteById(Authentication authentication,
                           Long id) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        taskService.deleteByAppUserAndId(appUser, id);
        appUserService.updateLastActionTime(appUser);
        return id;
    }

    @Override
    public BaseDto start(Authentication authentication,
                         Long id) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        Task task = taskService.findByAppUserAndId(appUser, id);
        Site site = task.getSite();

        TaskState taskState = task.getTaskState();
        SiteStatus siteStatus = site.getStatus();
        int order = task
                .getType()
                .getOrder();

        // If the task is sending
        if (taskState.equals(TaskState.SENDING))
            throw new BadRequestException("You cannot start a task that is already sending");

        // If the task is starting
        if (taskState.equals(TaskState.START))
            throw new BadRequestException("You cannot start a task that is already starting");

        // Any task is already sending for the site
        if (siteStatus.equals(SiteStatus.PROCESSING))
            throw new BadRequestException("You cannot start a task, a task is already sending for the site");

        // The previous task is not successfully completed
        List<Task> siteTasks = taskService.findAllByAppUserAndSite(appUser, site);
        if (siteTasks
                .stream()
                .noneMatch(t -> order == 0
                        || (t
                        .getType()
                        .getOrder() == order - 1
                        && t
                        .getTaskState()
                        .equals(TaskState.COMPLETE))))
            throw new BadRequestException("You cannot start a task for which a previous task has not been finished");

        getQueue(task).ifPresent(serviceQueueProperties -> {
            taskSenderService.send(task.toBody(), serviceQueueProperties, TaskAction.START.name());
            logSender.info("TASK SEND / Id: %s / Action: %s / Type: %s", task.getId(), TaskAction.START.name(),
                           task.getType());
            siteService.updateStatus(site, SiteStatus.PROCESSING);
            taskService.resetTask(task, TaskState.SENDING);
        });
        appUserService.updateLastActionTime(appUser);
        return task.toBaseDto();
    }

    @Override
    public BaseDto stop(Authentication authentication,
                        Long id) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        Task task = taskService.findByAppUserAndId(appUser, id);
        Site site = task.getSite();

        TaskState taskState = task.getTaskState();

        // If the task is starting
        if (!taskState.equals(TaskState.START)) throw new BadRequestException(
                "You cannot stop a task that has not yet started");

        getQueue(task).ifPresent(serviceQueueProperties -> {
            taskSenderService.send(task.toBody(), serviceQueueProperties, TaskAction.STOP.name());
            logSender.info("TASK SEND / Id: %s / Action: %s / Type: %s", task.getId(), TaskAction.STOP.name(),
                           task.getType());
        });
        appUserService.updateLastActionTime(appUser);
        return task.toBaseDto();
    }

    private Optional<TaskManagerProperties.ServiceQueueProperties> getQueue(Task task) {
        //  @formatter:off
        if (task.getType().equals(ServiceType.CRAWLER))
            return Optional.of(properties.getCrawler());
        if (task.getType().equals(ServiceType.INDEXER))
            return Optional.of(properties.getIndexer());
        if (task.getType().equals(ServiceType.SEARCHER))
            return Optional.of(properties.getSearcher());
        return Optional.empty();
        //  @formatter:on
    }

    private ServiceType convertStringTypeToResourceType(String type) {
        try {
            return ServiceType.valueOf(type.toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            throw new BadRequestException(
                    "Type of settings: " + type + " is invalid. Use one of [crawler, indexer, searcher]");
        }
    }
}
