package ru.malygin.taskmanager.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.malygin.taskmanager.exception.BadRequestException;
import ru.malygin.taskmanager.facade.TaskFacade;
import ru.malygin.taskmanager.model.ResourceType;
import ru.malygin.taskmanager.model.TaskState;
import ru.malygin.taskmanager.model.dto.BaseDto;
import ru.malygin.taskmanager.model.dto.impl.TaskDto;
import ru.malygin.taskmanager.model.entity.impl.AppUser;
import ru.malygin.taskmanager.model.entity.impl.Setting;
import ru.malygin.taskmanager.model.entity.impl.Site;
import ru.malygin.taskmanager.model.entity.impl.Task;
import ru.malygin.taskmanager.service.AppUserService;
import ru.malygin.taskmanager.service.SettingsService;
import ru.malygin.taskmanager.service.SiteService;
import ru.malygin.taskmanager.service.TaskService;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskFacadeImpl implements TaskFacade {

    private final AppUserService appUserService;
    private final TaskService taskService;
    private final SiteService siteService;
    private final SettingsService settingsService;

    @Override
    public BaseDto save(Authentication authentication,
                        TaskDto taskDto) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        Site site = siteService.findById(appUser, taskDto.getSiteId());
        Setting setting = settingsService.findById(appUser, taskDto.getSettingId());
        ResourceType resourceType = setting.getType();

        Task task = (Task) taskDto.toBaseEntity();
        task.setAppUser(appUser);
        task.setSite(site);
        task.setSetting(setting);
        task.setType(resourceType);
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
        ResourceType resourceType = convertStringTypeToResourceType(type);
        return taskService
                .findAllByAppUserAndResourceType(appUser, resourceType)
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

    private ResourceType convertStringTypeToResourceType(String type) {
        try {
            return ResourceType.valueOf(type.toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            throw new BadRequestException(
                    "Type of settings: " + type + " is invalid. Use one of [crawler, indexer, searcher]");
        }
    }
}
